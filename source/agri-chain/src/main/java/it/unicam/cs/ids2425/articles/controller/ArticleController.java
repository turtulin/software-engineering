package it.unicam.cs.ids2425.articles.controller;

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
import it.unicam.cs.ids2425.utilities.statuses.StatusInfo;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.statuses.controller.StatusInfoController;
import it.unicam.cs.ids2425.utilities.wrappers.Pair;
import it.unicam.cs.ids2425.utilities.wrappers.TypeToken;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

@NoArgsConstructor
public class ArticleController implements IController {
    private final SingletonRepository<List<IArticle>, IArticle, IArticle> articleRepository = SingletonRepository.getInstance(new TypeToken<>(){});
    private final SingletonRepository<Set<Pair<IArticle, List<StatusInfo<ArticleStatus>>>>, Pair<IArticle, List<StatusInfo<ArticleStatus>>>, IArticle> articleStatusRepository = SingletonRepository.getInstance(new TypeToken<>(){});

    private final SellerController sellerController = SingletonController.getInstance(new SellerController() {});
    private final StatusInfoController<ArticleStatus> statusInfoController = SingletonController.getInstance(new StatusInfoController<ArticleStatus>() {});

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

        List<IArticle> result = switch (user.getRole()) {
            case PRODUCER, TRANSFORMER, DISTRIBUTOR, EVENT_PLANNER -> Stream.of(
                            getAllArticles(ArticleStatus.DRAFT), getAllArticles(ArticleStatus.REJECTED), getAllArticles(ArticleStatus.PUBLISHED),
                            getAllArticles(ArticleStatus.PENDING)).flatMap(List::stream)
                    .filter(article -> article.getSeller().equals(user)).toList();
            case MODERATOR -> getAllArticles(ArticleStatus.PENDING);
            case CUSTOMER_SERVICE -> Stream.of(
                    getAllArticles(ArticleStatus.DRAFT), getAllArticles(ArticleStatus.REJECTED), getAllArticles(ArticleStatus.PUBLISHED),
                    getAllArticles(ArticleStatus.PENDING)).flatMap(List::stream).toList();
            case ADMIN -> getAllArticles(null);
            default -> throw new IllegalStateException("Unexpected value: " + u.getRole());
        };

        if (status != null){
            return result.stream().filter(article -> articleStatusRepository.get(article).getValue().getLast().status() == status).toList();
        }
        return result;
    }

    public String shareContent(@NonNull IArticle a) {
        // TODO: review code, minimal implementation
        a = get(a, ArticleStatus.PUBLISHED);
        if (a == null){
            throw new IllegalArgumentException("Article not found");
        }
        return a.toString();
    }

    private List<IArticle> getAllArticles(ArticleStatus status){
        if (status == null){
            return articleRepository.getAll();
        }
        return articleStatusRepository.getAll().stream().sorted()
                .filter(pair -> pair.getValue().getLast().status() == status)
                .map(Pair::getKey).map(articleRepository::get).toList();
    }

    public IArticle get(@NonNull IArticle article, ArticleStatus status){
        if (status != null) {
            check(article, status);
        }
        return articleRepository.get(article);
    }

    public IArticle create(@NonNull IArticle a, @NonNull ISeller s){
        IUser u = sellerController.get(s, UserStatus.ACTIVE);
        if (!sellerRoles.contains(u.getRole())){
            throw new IllegalArgumentException("User not allowed to create articles");
        }
        s = (ISeller) u;
        IArticle notExistent = get(a, null);
        if (notExistent != null){
            throw new IllegalArgumentException("Article already exists");
        }

        if (a.getSeller() == null || !a.getSeller().equals(s)){
            throw new IllegalArgumentException("Article seller must be the same as the user");
        }

        StatusInfo<ArticleStatus> statusInfo = statusInfoController.create(new StatusInfo<>(ArticleStatus.DRAFT, s), s);

        Pair<IArticle, List<StatusInfo<ArticleStatus>>> pair = new Pair<>(a, List.of(statusInfo));
        articleRepository.create(a);
        articleStatusRepository.create(pair);

        return get(a, null);
    }

    private IArticle genericUpdateArticle(@NonNull IArticle a, @NonNull IUser u, ArticleStatus newStatus, String newStatusReason, Consumer<IArticle> update){
        u = sellerController.get(u, UserStatus.ACTIVE);
        if (!sellerRoles.contains(u.getRole()) && !adminRoles.contains(u.getRole())){
            throw new IllegalArgumentException("User not allowed to update articles");
        }
        check(a, null);

        a = get(a, null);

        if (sellerRoles.contains(u.getRole()) && !a.getSeller().equals(u)){
            throw new IllegalArgumentException("Seller not allowed to update this article");
        }

        if (update != null){
            update.accept(a);
        }
        articleRepository.save(a, a);

        if (newStatus != null){
            Pair<IArticle, List<StatusInfo<ArticleStatus>>> pair = articleStatusRepository.get(a);
            StatusInfo<ArticleStatus> statusInfo = statusInfoController.create(new StatusInfo<>(newStatus, u, newStatusReason), u);
            pair.getValue().add(statusInfo);
            articleStatusRepository.save(a, pair);
        }

        return get(a, null);
    }

    private IArticle genericUpdateArticle(@NonNull IArticle a, @NonNull IUser u, ArticleStatus newStatus, String newStatusReason){
        return genericUpdateArticle(a, u, newStatus, newStatusReason, null);
    }

    private IArticle genericUpdateArticle(@NonNull IArticle a, @NonNull IUser u, Consumer<IArticle> update){
        return genericUpdateArticle(a, u, null, null, update);
    }

    public IArticle updateArticle(@NonNull IArticle a, @NonNull ISeller s){
        return genericUpdateArticle(a, s, ArticleStatus.DRAFT, "Article updated");
    }

    public IArticle publishArticle(@NonNull IArticle a, @NonNull ISeller s){
        return genericUpdateArticle(a, s, ArticleStatus.PENDING, "Article published");
    }

    public IArticle approveArticle(@NonNull IArticle a, @NonNull Moderator m){
        return genericUpdateArticle(a, m, ArticleStatus.PUBLISHED, "Article approved");
    }

    public @NonNull IArticle rejectArticle(@NonNull IArticle a, @NonNull String reason, @NonNull Moderator m) {
        return genericUpdateArticle(a, m, ArticleStatus.REJECTED, reason);
    }

    public IArticle draftArticle(@NonNull IArticle a, @NonNull ISeller s){
        return genericUpdateArticle(a, s, ArticleStatus.DRAFT, "Article drafted");
    }

    public IArticle deleteArticle(@NonNull IArticle a, @NonNull ISeller s){
        return genericUpdateArticle(a, s, ArticleStatus.DELETED, "Article deleted");
    }

    public IArticle reduceArticleQuantity(@NonNull IArticle a, @NonNull ISeller s, int quantity){
        return genericUpdateArticle(a, s, (IArticle art) -> art.setAvailableArticles(art.getAvailableArticles() - quantity));
    }

    private boolean check(@NonNull IArticle article, ArticleStatus status) {
        article = get(article, null);
        if (article == null) {
            throw new IllegalArgumentException("Article not found");
        }
        Pair<IArticle, List<StatusInfo<ArticleStatus>>> pair = articleStatusRepository.get(article);
        if (pair == null) {
            throw new IllegalArgumentException("Article status not found");
        }
        if(status != null && pair.getValue().getLast().status() != status){
            throw new IllegalArgumentException("Article status is not " + status);
        }
        return true;
    }

    public Pair<IArticle, List<StatusInfo<ArticleStatus>>> getArticleStatus(@NonNull IArticle a) {
        a = get(a, null);
        if (a == null){
            throw new IllegalArgumentException("Article not found");
        }
        return articleStatusRepository.get(a);
    }
}
