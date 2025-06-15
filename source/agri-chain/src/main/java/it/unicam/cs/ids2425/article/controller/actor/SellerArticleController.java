package it.unicam.cs.ids2425.article.controller.actor;

import it.unicam.cs.ids2425.article.controller.AbstractArticleController;
import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.model.ArticleState;
import it.unicam.cs.ids2425.article.model.ArticleType;
import it.unicam.cs.ids2425.article.model.HasComponent;
import it.unicam.cs.ids2425.article.model.article.compositearticle.ComposableArticle;
import it.unicam.cs.ids2425.article.repository.AnyArticleRepository;
import it.unicam.cs.ids2425.article.repository.ArticleRepository;
import it.unicam.cs.ids2425.article.repository.ArticleStateRepository;
import it.unicam.cs.ids2425.eshop.controller.stock.StockController;
import it.unicam.cs.ids2425.eshop.model.stock.Stock;
import it.unicam.cs.ids2425.eshop.model.stock.StockContent;
import it.unicam.cs.ids2425.user.controller.actor.SingleEntityController;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import jakarta.transaction.Transactional;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class SellerArticleController<T extends Article> extends AbstractArticleController<T> {
    private final StockController stockController;
    private final SingleEntityController singleEntityController;
    private final AnyArticleRepository anyArticleRepository;

    public SellerArticleController(ArticleStateRepository articleStatusRepository, ArticleRepository<T> articleRepository, StockController stockController, SingleEntityController singleEntityController, AnyArticleRepository anyArticleRepository) {
        super(articleStatusRepository, articleRepository);
        this.stockController = stockController;
        this.singleEntityController = singleEntityController;
        this.anyArticleRepository = anyArticleRepository;
    }

    public List<Article> getAllArticles(@NonNull ArticleStatusCode articleStatusCode, @NonNull User user) {
        return getAllArticles(user).stream()
                .map(article -> articleStateRepository.findAllByEntity(article).getLast())
                .filter(articleState -> articleState.getStatusCode().equals(articleStatusCode))
                .map(ArticleState::getEntity)
                .toList();
    }

    public List<Article> getAllArticles(@NonNull User user) {
        checkSeller(user);
        return articleRepository.findAllBySeller(user).stream()
                .map(article -> articleStateRepository.findAllByEntity(article).getLast())
                .filter(articleState -> !articleState.getStatusCode().equals(ArticleStatusCode.DELETED))
                .map(ArticleState::getEntity)
                .toList();
    }

    public Article getArticle(@NonNull Long articleId, @NonNull ArticleStatusCode status, @NonNull User user) {
        checkSeller(user);
        ArticleState articleState = articleStateRepository.findAllByEntity_Id(articleId).getLast();
        if (status.equals(ArticleStatusCode.DELETED) || status.equals(ArticleStatusCode.ARCHIVED)) {
            throw new IllegalArgumentException("Article not found");
        }
        if (articleState.getStatusCode().equals(status) && articleState.getEntity().getSeller().equals(user)) {
            return articleState.getEntity();
        }
        throw new IllegalArgumentException("Article not found");
    }

    @Transactional
    public ArticleState updateArticle(@NonNull Long articleId, @NonNull ArticleStatusCode articleStatusCode, @NonNull User user) {
        return updateArticle(articleId, articleStatusCode, user, null);
    }

    @Transactional
    public ArticleState updateArticle(@NonNull Long articleId, @NonNull ArticleStatusCode articleStatusCode, @NonNull User user, String reason) {
        checkSeller(user);
        ArticleState oldArticleState = articleStateRepository.findAllByEntity_Id(articleId).getLast();
        if (!oldArticleState.getEntity().getSeller().equals(user)) {
            throw new IllegalArgumentException("Article not found");
        }
        if (articleStatusCode == ArticleStatusCode.PENDING){
            if (oldArticleState.getEntity() instanceof HasComponent hasComponent) {
                if (hasComponent.getComponents() == null || hasComponent.getComponents().isEmpty()) {
                    throw new IllegalArgumentException(STR."Can't publish \{oldArticleState.getEntity().getType().toString()} without components");
                }
                for (Article component : hasComponent.getComponents()) {
                    try {
                        getArticleById(component.getId(), ArticleStatusCode.PUBLISHED);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Component not found");
                    }
                }
            }
        } else if (articleStatusCode != ArticleStatusCode.PUBLISHED) {
            // this excludes the status published and pending,
            //  resulting in the call of the following function only for the updates to draft and delete
            if (oldArticleState.getEntity() instanceof ComposableArticle composableArticle) {
                draftAllArticlesContaining(composableArticle);
            }
        }
        // no new entity created because this is the article status change, the entity data did not change.
        ArticleState newArticleState = new ArticleState(articleStatusCode, user, reason, oldArticleState.getEntity(), oldArticleState);
        articleStateRepository.save(newArticleState);
        return articleStateRepository.findAllByEntity_Id(articleId).getLast();
    }

    @Transactional
    public void draftAllArticlesContaining(ComposableArticle entity) {
       List<Article> articles = new ArrayList<>(anyArticleRepository.findAll().stream()
                .filter(a -> a instanceof HasComponent)
                .filter(a -> ((HasComponent) a).getComponents().stream()
                        .anyMatch(c -> c.equals(entity))
                ).toList());
        for (int i = 0; i<articles.size(); i++) {
            Article hasComponent = articles.get(i);
            ArticleState oldArticleState = articleStateRepository.findAllByEntity_Id(hasComponent.getId()).getLast();
            // no new entity created because this is the article drafting after one of their component was drafted, the entity data did not change.
            ArticleState newArticleState = new ArticleState(ArticleStatusCode.DRAFT, singleEntityController.getTimeUser(), "Component Changed", hasComponent, oldArticleState);
            articleStateRepository.save(newArticleState);
            if (hasComponent instanceof ComposableArticle composableArticle) {
                articles.addAll(anyArticleRepository.findAll().stream()
                        .filter(a -> a instanceof HasComponent)
                        .filter(a -> ((HasComponent) a).getComponents().stream()
                                .anyMatch(c -> c.equals(composableArticle))
                        ).filter(a -> !articles.contains(a)).toList());
            }
        }
    }

    @Deprecated
    @Transactional
    public void draftAllArticlesContaining(ComposableArticle entity, List<Article> exclusions) {
        List<Article> articles = anyArticleRepository.findAll().stream()
                .filter(a -> a instanceof HasComponent)
                .filter(a -> ((HasComponent) a).getComponents().stream()
                        .anyMatch(c -> c.equals(entity))
                ).filter(a -> !exclusions.contains(a))
                .toList();
        // as the function is recursive,
        //  avoided using exclusions.addAll to not apply lower dependencies to upper calls of this function
        // WARNING: this affects memory usage
        List<Article> newExclusions = new ArrayList<>(exclusions);
        newExclusions.addAll(articles);
        for (Article hasComponent : articles) {
            ArticleState oldArticleState = articleStateRepository.findAllByEntity_Id(hasComponent.getId()).getLast();
            // no new entity created because this is the article drafting after one of their component was drafted, the entity data did not change.
            ArticleState newArticleState = new ArticleState(ArticleStatusCode.DRAFT, singleEntityController.getTimeUser(), "Component Changed", hasComponent, oldArticleState);
            articleStateRepository.save(newArticleState);
            if (hasComponent instanceof ComposableArticle composableArticle) {
                // TODO check if it is possible to simplify, don't like this solution
                // WARNING: recursive call here, may cause stack overflow
                // excluding "articles" avoids duplicate changes for
                //  example in:
                //      - recursive articles (should never happen, code should not allow it, can only add published articles as components)
                //      - cross dependencies (article A may be a component to articles B and C, and article B may be component of article C,
                //          we only need to draft A and B once)
                // Note: the presence of the growing "exclusions" list effectively reduces the input set over recursive calls
                draftAllArticlesContaining(composableArticle, newExclusions);
            }
        }
    }

    protected abstract void checkSeller(@NonNull User user);

    protected abstract boolean notCorrectArticleType(T article);

    @Transactional
    public Article createArticle(@NonNull T article, @NonNull User user) {
        checkSeller(user);

        if (articleRepository.findByName(article.getName()).isPresent()) {
            throw new IllegalArgumentException("Article already exists");
        }

        article.setSeller(user);

        if (article.getType() == null || notCorrectArticleType(article)) {
            article.setType(ArticleType.fromUserRole(user.getRole()));
        }

        if (article instanceof HasComponent hasComponent) {
            List<ComposableArticle> components = hasComponent.getComponents();
            if (components != null) {
                for (Article component : components) {
                    try {
                        getArticleById(component.getId(), ArticleStatusCode.PUBLISHED);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Component not found");
                    }
                }
            }
        }

        ArticleState state = new ArticleState(ArticleStatusCode.DRAFT, user, null, article, null);
        articleRepository.save(article);
        articleStateRepository.save(state);

        return getArticleById(article.getId(), ArticleStatusCode.DRAFT);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Article updateArticle(@NonNull T articleDelta, @NonNull User user) {
        checkSeller(user);

        if (articleDelta.getName() == null && articleDelta.getDescription() == null && articleDelta.getPrice() == null) {
            throw new IllegalArgumentException("Article must have at least one field to update");
        }

        if (articleRepository.findById(articleDelta.getId()).isEmpty()) {
            throw new IllegalArgumentException("Article not found");
        }

        T oldArticle = articleRepository.findById(articleDelta.getId()).get();

        if (!oldArticle.getSeller().equals(user)) {
            throw new IllegalArgumentException("Article seller must be the same as the user");
        }

        if (notCorrectArticleType(oldArticle)) {
            throw new IllegalArgumentException("Article type must be specified");
        }

        ArticleState oldState = articleStateRepository.findAllByEntity(oldArticle).getLast();

        // suppressing unchecked cast warning, this is unavoidable, but we know that Article and all its subclasses are cloneable.
        T newArticle = (T) oldArticle.clone();

        if (articleDelta.getName() != null) {
            newArticle.setName(articleDelta.getName());
        }
        if (articleDelta.getDescription() != null) {
            newArticle.setDescription(articleDelta.getDescription());
        }
        if (articleDelta.getPrice() != null) {
            newArticle.setPrice(articleDelta.getPrice());
        }

        if (articleDelta instanceof HasComponent hasComponent){
            if (newArticle instanceof HasComponent toUpdateHasComponent) {
                if (hasComponent.getComponents() != null) {
                    toUpdateHasComponent.setComponents(hasComponent.getComponents());
                }
            } else {
                throw new IllegalArgumentException("Article type does not match");
            }
        }

        ArticleState oldArticleState = new ArticleState(ArticleStatusCode.ARCHIVED, user, "updated", oldArticle, oldState);
        articleStateRepository.save(oldArticleState);

        newArticle = articleRepository.save(newArticle);

        ArticleState state = new ArticleState(ArticleStatusCode.DRAFT, user, "updated", newArticle, oldArticleState);
        articleStateRepository.save(state);

        if (oldArticle instanceof ComposableArticle composableArticle) {
            draftAllArticlesContaining(composableArticle);
        }

        return getArticleById(newArticle.getId(), ArticleStatusCode.DRAFT);
    }

    @Transactional
    public Article updateArticleQuantity(@NonNull Long articleId, @NonNull Long quantity, @NonNull User user) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        return changeStockQuantity(articleId, quantity, user).getArticle();
    }

    public Long getArticleQuantity(@NonNull Long articleId, @NonNull User user) {
        List<StockContent> stockContents = stockController.findByUser(user).getArticles();
        return stockContents.stream().filter(sc -> sc.getArticle().getId().equals(articleId) && sc.getArticle().getSeller().equals(user))
                .map(StockContent::getQuantity).findFirst().orElse(0L);
    }

    @Transactional
    protected StockContent changeStockQuantity(@NonNull Long articleId, @NonNull Long quantity, @NonNull User user) {
        Article article = getArticleById(articleId, ArticleStatusCode.PUBLISHED);
        if (!article.getSeller().equals(user)) {
            throw new IllegalArgumentException("Article not found");
        }
        Stock stock = stockController.findByUser(user);
        return stockController.changeQuantity(stock, article, quantity);
    }
}
