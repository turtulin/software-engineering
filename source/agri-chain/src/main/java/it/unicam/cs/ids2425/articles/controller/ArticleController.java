package it.unicam.cs.ids2425.articles.controller;

import it.unicam.cs.ids2425.articles.model.articles.ArticleState;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.actors.Moderator;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@NoArgsConstructor
public class ArticleController implements IController {
    private final SingletonRepository<IArticle> articleRepository = SingletonRepository.getInstance(IArticle.class);
    private final SingletonRepository<ArticleState> articleStatusRepository = SingletonRepository.getInstance(ArticleState.class);

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

        if (article.getSeller() == null || !article.getSeller().equals(seller)) {
            throw new IllegalArgumentException("Article seller must be the same as the user");
        }

        ArticleState state = new ArticleState(article, ArticleStatus.DRAFT, seller);
        articleRepository.save(article);
        articleStatusRepository.save(state);

        return get(article, ArticleStatus.DRAFT);
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

    public IArticle updateArticle(@NonNull IArticle article, @NonNull ISeller seller) {
        return genericUpdateArticle(article, seller, ArticleStatus.DRAFT, "Article updated");
    }

    public IArticle publishArticle(@NonNull IArticle article, @NonNull ISeller seller) {
        return genericUpdateArticle(article, seller, ArticleStatus.PENDING, "Article published");
    }

    public IArticle approveArticle(@NonNull IArticle article, @NonNull Moderator moderator) {
        return genericUpdateArticle(article, moderator, ArticleStatus.PUBLISHED, "Article approved");
    }

    public @NonNull IArticle rejectArticle(@NonNull IArticle article, @NonNull String reason, @NonNull Moderator moderator) {
        return genericUpdateArticle(article, moderator, ArticleStatus.REJECTED, reason);
    }

    public IArticle draftArticle(@NonNull IArticle article, @NonNull ISeller seller) {
        return genericUpdateArticle(article, seller, ArticleStatus.DRAFT, "Article drafted");
    }

    public IArticle deleteArticle(@NonNull IArticle article, @NonNull ISeller seller) {
        return genericUpdateArticle(article, seller, ArticleStatus.DELETED, "Article deleted");
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
