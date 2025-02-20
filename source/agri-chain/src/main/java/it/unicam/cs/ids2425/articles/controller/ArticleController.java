package it.unicam.cs.ids2425.articles.controller;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import it.unicam.cs.ids2425.articles.model.articles.Package;
import it.unicam.cs.ids2425.articles.model.articles.*;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks.DistributorStockController;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks.EventPlannerStockController;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks.ProducerStockController;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks.TransformerStockController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.actors.Moderator;
import it.unicam.cs.ids2425.users.model.actors.sellers.*;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@NoArgsConstructor
public class ArticleController implements IController {
    private static final Map<UserRole, ArticleType> userRoleArticleTypeMap = Map.of(
            UserRole.PRODUCER, ArticleType.RAW_MATERIAL,
            UserRole.TRANSFORMER, ArticleType.PROCESSED_PRODUCT,
            UserRole.DISTRIBUTOR, ArticleType.PACKAGE,
            UserRole.EVENT_PLANNER, ArticleType.EVENT
    );
    private final SingletonRepository<IArticle> articleRepository = SingletonRepository.getInstance(IArticle.class);
    private final SingletonRepository<ArticleState> articleStatusRepository = SingletonRepository.getInstance(ArticleState.class);
    private final ProducerStockController producerStockController = SingletonController.getInstance(new ProducerStockController());
    private final TransformerStockController transformerStockController = SingletonController.getInstance(new TransformerStockController());
    private final DistributorStockController distributorStockController = SingletonController.getInstance(new DistributorStockController());
    private final EventPlannerStockController eventPlannerStockController = SingletonController.getInstance(new EventPlannerStockController());
    private final List<UserRole> sellerRoles = List.of(UserRole.PRODUCER, UserRole.TRANSFORMER, UserRole.DISTRIBUTOR, UserRole.EVENT_PLANNER);
    private final List<UserRole> adminRoles = List.of(UserRole.ADMIN, UserRole.CUSTOMER_SERVICE, UserRole.MODERATOR);

    public List<IArticle> getAll(@NonNull IUser user) {
        return getAll(user, null);
    }

    public List<IArticle> getAll(@NonNull IUser user, ArticleStatus status) {
        if (user.getRole() == UserRole.GUEST) {
            return getAllArticles(ArticleStatus.PUBLISHED);
        }
        if (user.getRole() == UserRole.CUSTOMER) {
            return getAllArticles(ArticleStatus.PUBLISHED);
        }

        List<IArticle> result = new ArrayList<>();
        if (sellerRoles.contains(user.getRole())) {
            result.addAll(getAllArticles(null).stream()
                    .filter(article -> article.getSeller().equals(user) &&
                            getArticleStatus(article) != ArticleStatus.DELETED).toList());
        }
        if (user.getRole() == UserRole.MODERATOR) {
            result.addAll(getAllArticles(ArticleStatus.PENDING));
        }
        if (user.getRole() == UserRole.CUSTOMER_SERVICE) {
            result.addAll(getAllArticles(null).stream()
                    .filter(article -> getArticleStatus(article) != ArticleStatus.DELETED).toList());
        }
        if (user.getRole() == UserRole.ADMIN) {
            result.addAll(getAllArticles(null));
        }

        if (status != null) {
            return result.stream().filter(article -> articleStatusRepository.findAll().stream()
                    .filter(state -> state.getEntity().equals(article))
                    .sorted().toList().getLast().getStatus() == status).toList();
        }
        return result;
    }

    public String shareContent(@NonNull IArticle article) {
        // TODO: review code, minimal implementation
        article = get(article, ArticleStatus.PUBLISHED);
        if (article == null) {
            throw new NoSuchElementException("Article not found");
        }
        return article.toString();
    }

    private List<IArticle> getAllArticles(ArticleStatus status) {
        if (status == null) {
            return articleRepository.findAll();
        }
        return articleRepository.findAll().stream()
                .filter(article -> {
                    List<ArticleState> articles = articleStatusRepository.findAll().stream()
                            .filter(state -> state.getEntity().equals(article))
                            .sorted().toList();
                    return articles.getLast().getStatus() == status;
                }).toList();
    }

    public IArticle get(@NonNull IArticle article, ArticleStatus status) {
        check(article, status);
        return articleRepository.findById(article).orElseThrow(() -> new NoSuchElementException("Article not found"));
    }

    public IArticle create(@NonNull IArticle article, @NonNull ISeller seller) {
        if (!sellerRoles.contains(seller.getRole())) {
            throw new IllegalArgumentException("User not allowed to create articles");
        }
        try {
            get(article, null);
            throw new IllegalArgumentException("Article already exists");
        } catch (NoSuchElementException ignored) {
        }

        checkUserArticlePermission(article, seller);

        if (article.getSeller() == null || !article.getSeller().equals(seller)) {
            throw new IllegalArgumentException("Article seller must be the same as the user");
        }

        ArticleState state = new ArticleState(article, ArticleStatus.DRAFT, seller);
        articleRepository.save(article);
        articleStatusRepository.save(state);

        return get(article, ArticleStatus.DRAFT);
    }

    public IArticle setArticleQuantity(@NonNull IArticle article, @NonNull ISeller seller, @NonNull Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        checkUserArticlePermission(article, seller);
        return genericUpdateArticle(article, seller, a -> {
            switch (a.getType()) {
                case ArticleType.RAW_MATERIAL ->
                        producerStockController.addArticleToStock((RawMaterial) article, (Producer) seller, quantity);
                case ArticleType.PROCESSED_PRODUCT ->
                        transformerStockController.addArticleToStock((ProcessedProduct) article, (Transformer) seller, quantity);
                case ArticleType.PACKAGE ->
                        distributorStockController.addArticleToStock((Package) article, (Distributor) seller, quantity);
                case ArticleType.EVENT ->
                        eventPlannerStockController.addArticleToStock((Event) article, (EventPlanner) seller, quantity);
            }
        });
    }

    private IArticle genericUpdateArticle(@NonNull IArticle article, @NonNull IUser user, ArticleStatus newStatus, String newStatusReason, Consumer<IArticle> update) {
        if (!(sellerRoles.contains(user.getRole()) || adminRoles.contains(user.getRole()))) {
            throw new IllegalArgumentException("User not allowed to update articles");
        }
        IArticle a = get(article, null);

        if (update != null) {
            if (!(sellerRoles.contains(user.getRole()) && a.getSeller().equals(user))) {
                throw new IllegalArgumentException("Seller not allowed to update this article");
            }
            update.accept(a);
            articleRepository.save(a);
        }

        if (newStatus != null) {
            ArticleState oldState = articleStatusRepository.findAll().stream()
                    .filter(state -> state.getEntity().equals(a))
                    .sorted().toList().getLast();
            ArticleState state = new ArticleState(a, newStatus, user, newStatusReason, oldState);
            articleStatusRepository.save(state);
            return get(a, newStatus);
        }

        return get(a, null);
    }

    private IArticle genericUpdateArticle(@NonNull IArticle article, @NonNull IUser user, ArticleStatus newStatus, String newStatusReason) {
        return genericUpdateArticle(article, user, newStatus, newStatusReason, null);
    }

    private IArticle genericUpdateArticle(@NonNull IArticle article, @NonNull ISeller seller, @NonNull Consumer<IArticle> update) {
        return genericUpdateArticle(article, seller, null, null, update);
    }

    public IArticle updateArticle(@NonNull IArticle article, @NonNull ISeller seller) {
        checkUserArticlePermission(article, seller);
        return genericUpdateArticle(article, seller, ArticleStatus.DRAFT, "Article updated");
    }

    public IArticle publishArticle(@NonNull IArticle article, @NonNull ISeller seller) {
        checkUserArticlePermission(article, seller);
        return genericUpdateArticle(article, seller, ArticleStatus.PENDING, "Article published");
    }

    public IArticle draftArticle(@NonNull IArticle article, @NonNull ISeller seller) {
        checkUserArticlePermission(article, seller);
        return genericUpdateArticle(article, seller, ArticleStatus.DRAFT, "Article drafted");
    }

    public IArticle deleteArticle(@NonNull IArticle article, @NonNull ISeller seller) {
        checkUserArticlePermission(article, seller);
        return genericUpdateArticle(article, seller, ArticleStatus.DELETED, "Article deleted");
    }

    public IArticle approveArticle(@NonNull IArticle article, @NonNull Moderator moderator) {
        return genericUpdateArticle(article, moderator, ArticleStatus.PUBLISHED, "Article approved");
    }

    public @NonNull IArticle rejectArticle(@NonNull IArticle article, @NonNull String reason, @NonNull Moderator moderator) {
        return genericUpdateArticle(article, moderator, ArticleStatus.REJECTED, reason);
    }

    public ArticleStatus getArticleStatus(@NonNull IArticle article) {
        IArticle a = get(article, null);
        if (a == null) {
            throw new IllegalArgumentException("Article not found");
        }
        return articleStatusRepository.findAll().stream()
                .filter(state -> state.getEntity().equals(a))
                .sorted().toList().getLast().getStatus();
    }

    private void check(@NonNull IArticle article, ArticleStatus status) {
        List<ArticleState> state = articleStatusRepository.findAll().stream()
                .filter(s -> s.getEntity().equals(article))
                .sorted().toList();
        if (state.isEmpty()) {
            throw new NoSuchElementException("Article not found");
        }
        if (status != null && state.getLast().getStatus() != status) {
            throw new IllegalArgumentException("Article status is not " + status);
        }
    }

    private void checkUserArticlePermission(@NonNull IArticle article, @NonNull ISeller seller) {
        if (article.getType() != userRoleArticleTypeMap.get(seller.getRole())) {
            throw new IllegalArgumentException("Article type not allowed for this user");
        }
    }

    public Integer getArticleQuantity(IArticle article, ISeller seller) {
        checkUserArticlePermission(article, seller);
        return switch (article.getType()) {
            case RAW_MATERIAL -> producerStockController.getArticleQuantity((RawMaterial) article, (Producer) seller);
            case PROCESSED_PRODUCT ->
                    transformerStockController.getArticleQuantity((ProcessedProduct) article, (Transformer) seller);
            case PACKAGE -> distributorStockController.getArticleQuantity((Package) article, (Distributor) seller);
            case EVENT -> eventPlannerStockController.getArticleQuantity((Event) article, (EventPlanner) seller);
        };
    }
}
