package it.unicam.cs.ids2425.article.controller.actor;

import it.unicam.cs.ids2425.article.controller.AbstractArticleController;
import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.model.ArticleState;
import it.unicam.cs.ids2425.article.model.HasComponent;
import it.unicam.cs.ids2425.article.repository.ArticleRepository;
import it.unicam.cs.ids2425.article.repository.ArticleStateRepository;
import it.unicam.cs.ids2425.eshop.controller.stock.StockController;
import it.unicam.cs.ids2425.eshop.model.stock.Stock;
import it.unicam.cs.ids2425.eshop.model.stock.StockContent;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import jakarta.transaction.Transactional;
import lombok.NonNull;

import java.util.List;

public abstract class SellerArticleController<T extends Article> extends AbstractArticleController<T> {
    private final StockController stockController;

    public SellerArticleController(ArticleStateRepository articleStatusRepository, ArticleRepository<T> articleRepository, StockController stockController) {
        super(articleStatusRepository, articleRepository);
        this.stockController = stockController;
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
        if (articleState.getStatusCode().equals(status) && articleState.getEntity().getSeller().equals(user)) {
            return articleState.getEntity();
        }
        throw new IllegalArgumentException("Article not found");
    }

    public ArticleState updateArticle(@NonNull Long articleId, @NonNull ArticleStatusCode articleStatusCode, @NonNull User user) {
        checkSeller(user);
        ArticleState oldArticleState = articleStateRepository.findAllByEntity_Id(articleId).getLast();
        if (!oldArticleState.getEntity().getSeller().equals(user)) {
            throw new IllegalArgumentException("Article not found");
        }
        ArticleState newArticleState = new ArticleState(articleStatusCode, user, null, oldArticleState.getEntity(), oldArticleState);
        articleStateRepository.save(newArticleState);
        return articleStateRepository.findAllByEntity_Id(articleId).getLast();
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
            throw new IllegalArgumentException("Article type must be specified");
        }

        ArticleState state = new ArticleState(ArticleStatusCode.DRAFT, user, null, article, null);
        articleRepository.save(article);
        articleStateRepository.save(state);

        return getArticleById(article.getId(), ArticleStatusCode.DRAFT);
    }

    @Transactional
    public Article updateArticle(@NonNull T article, @NonNull User user) {
        checkSeller(user);

        if (articleRepository.findById(article.getId()).isEmpty()) {
            throw new IllegalArgumentException("Article not found");
        }
        T toUpdateArticle = articleRepository.findById(article.getId()).get();

        if (!toUpdateArticle.getSeller().equals(user)) {
            throw new IllegalArgumentException("Article seller must be the same as the user");
        }

        if (notCorrectArticleType(toUpdateArticle)) {
            throw new IllegalArgumentException("Article type must be specified");
        }

        ArticleState oldState = articleStateRepository.findAllByEntity(toUpdateArticle).getLast();

        if (article.getName() != null) {
            toUpdateArticle.setName(article.getName());
        }
        if (article.getDescription() != null) {
            toUpdateArticle.setDescription(article.getDescription());
        }
        if (article.getPrice() != null) {
            toUpdateArticle.setPrice(article.getPrice());
        }

        if (article instanceof HasComponent hasComponent && toUpdateArticle instanceof HasComponent toUpdateHasComponent) {
            if (hasComponent.getComponents() != null) {
                toUpdateHasComponent.setComponents(hasComponent.getComponents());
            }
        }
        ArticleState state = new ArticleState(ArticleStatusCode.DRAFT, user, "updated", toUpdateArticle, oldState);

        articleRepository.save(toUpdateArticle);
        articleStateRepository.save(state);

        return getArticleById(article.getId(), ArticleStatusCode.DRAFT);
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
        return stockContents.get(
                stockContents.indexOf(new StockContent(
                        articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("Article not found")),
                        0L))).getQuantity();
    }

    @Transactional
    protected StockContent changeStockQuantity(@NonNull Long articleId, @NonNull Long quantity, @NonNull User user) {
        Article article = getArticleById(articleId, ArticleStatusCode.PUBLISHED);
        Stock stock = stockController.findByUser(user);
        return stockController.changeQuantity(stock, article, quantity);
    }
}
