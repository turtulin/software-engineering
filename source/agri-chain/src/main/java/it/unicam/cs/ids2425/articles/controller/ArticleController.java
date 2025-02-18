package it.unicam.cs.ids2425.articles.controller;

import it.unicam.cs.ids2425.articles.model.articles.ArticleState;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.users.controller.actors.sellers.SellerController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.actors.Moderator;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.statuses.State;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

@NoArgsConstructor
public class ArticleController implements IController {
    private final SingletonRepository<IArticle> articleRepository = SingletonRepository.getInstance(IArticle.class);
    private final SingletonRepository<ArticleState> articleStatusRepository = SingletonRepository.getInstance(ArticleState.class);

    private final SellerController sellerController = SingletonController.getInstance(new SellerController() {
    });

    private final List<UserRole> sellerRoles = List.of(UserRole.PRODUCER, UserRole.TRANSFORMER, UserRole.DISTRIBUTOR, UserRole.EVENT_PLANNER);
    private final List<UserRole> adminRoles = List.of(UserRole.ADMIN, UserRole.CUSTOMER_SERVICE, UserRole.MODERATOR);

    public List<IArticle> getAll(@NonNull IUser u) {
        return getAll(u, null);
    }

    public List<IArticle> getAll(@NonNull IUser u, ArticleStatus status) {
        if (u.getRole() == UserRole.GUEST) {
            return getAllArticles(ArticleStatus.PUBLISHED);
        }
        IUser user = sellerController.get(u, UserStatus.ACTIVE);
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

    public String shareContent(@NonNull IArticle a) {
        // TODO: review code, minimal implementation
        a = get(a, ArticleStatus.PUBLISHED);
        if (a == null) {
            throw new IllegalArgumentException("Article not found");
        }
        return a.toString();
    }

    private List<IArticle> getAllArticles(ArticleStatus status) {
        if (status == null) {
            return articleRepository.findAll();
        }
        return articleRepository.findAll().stream()
                .map(article ->
                        articleStatusRepository.findAll().stream()
                                .filter(state -> state.getEntity().equals(article))
                                .sorted().toList().getLast())
                .filter(state -> state.getStatus() == status)
                .map(State::getEntity)
                .map(articleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get).toList();
    }

    public IArticle get(@NonNull IArticle article, ArticleStatus status) {
        if (status != null) {
            check(article, status);
        }
        return articleRepository.findById(article).orElseThrow(() -> new NoSuchElementException("Article not found"));
    }

    public IArticle create(@NonNull IArticle a, @NonNull ISeller s) {
        IUser u = sellerController.get(s, UserStatus.ACTIVE);
        if (!sellerRoles.contains(u.getRole())) {
            throw new IllegalArgumentException("User not allowed to create articles");
        }
        s = (ISeller) u;
        IArticle notExistent = get(a, null);
        if (notExistent != null) {
            throw new IllegalArgumentException("Article already exists");
        }

        if (a.getSeller() == null || !a.getSeller().equals(s)) {
            throw new IllegalArgumentException("Article seller must be the same as the user");
        }

        ArticleState state = new ArticleState(a, ArticleStatus.DRAFT, s);
        articleRepository.save(a);
        articleStatusRepository.save(state);

        return get(a, ArticleStatus.DRAFT);
    }

    private IArticle genericUpdateArticle(@NonNull IArticle article, @NonNull IUser u, ArticleStatus newStatus, String newStatusReason, Consumer<IArticle> update) {
        u = sellerController.get(u, UserStatus.ACTIVE);
        if (!sellerRoles.contains(u.getRole()) && !adminRoles.contains(u.getRole())) {
            throw new IllegalArgumentException("User not allowed to update articles");
        }
        check(article, null);
        IArticle a = get(article, null);

        if (sellerRoles.contains(u.getRole()) && !a.getSeller().equals(u)) {
            throw new IllegalArgumentException("Seller not allowed to update this article");
        }

        if (update != null) {
            update.accept(a);
        }
        articleRepository.save(a);

        if (newStatus != null) {
            ArticleState oldState = articleStatusRepository.findAll().stream()
                    .filter(state -> state.getEntity().equals(a))
                    .sorted().toList().getLast();
            ArticleState state = new ArticleState(a, newStatus, u, newStatusReason, oldState);
            articleStatusRepository.save(state);
            return get(a, newStatus);
        }

        return get(a, null);
    }

    private IArticle genericUpdateArticle(@NonNull IArticle a, @NonNull IUser u, ArticleStatus newStatus, String newStatusReason) {
        return genericUpdateArticle(a, u, newStatus, newStatusReason, null);
    }

    private IArticle genericUpdateArticle(@NonNull IArticle a, @NonNull IUser u, Consumer<IArticle> update) {
        return genericUpdateArticle(a, u, null, null, update);
    }

    public IArticle updateArticle(@NonNull IArticle a, @NonNull ISeller s) {
        return genericUpdateArticle(a, s, ArticleStatus.DRAFT, "Article updated");
    }

    public IArticle publishArticle(@NonNull IArticle a, @NonNull ISeller s) {
        return genericUpdateArticle(a, s, ArticleStatus.PENDING, "Article published");
    }

    public IArticle approveArticle(@NonNull IArticle a, @NonNull Moderator m) {
        return genericUpdateArticle(a, m, ArticleStatus.PUBLISHED, "Article approved");
    }

    public @NonNull IArticle rejectArticle(@NonNull IArticle a, @NonNull String reason, @NonNull Moderator m) {
        return genericUpdateArticle(a, m, ArticleStatus.REJECTED, reason);
    }

    public IArticle draftArticle(@NonNull IArticle a, @NonNull ISeller s) {
        return genericUpdateArticle(a, s, ArticleStatus.DRAFT, "Article drafted");
    }

    public IArticle deleteArticle(@NonNull IArticle a, @NonNull ISeller s) {
        return genericUpdateArticle(a, s, ArticleStatus.DELETED, "Article deleted");
    }

    private boolean check(@NonNull IArticle a, ArticleStatus status) {
        IArticle article = get(a, null);
        if (article == null) {
            throw new IllegalArgumentException("Article not found");
        }
        ArticleState state = articleStatusRepository.findAll().stream()
                .filter(s -> s.getEntity().equals(article))
                .sorted().toList().getLast();
        if (status != null && state.getStatus() != status) {
            throw new IllegalArgumentException("Article status is not " + status);
        }
        return true;
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
}
