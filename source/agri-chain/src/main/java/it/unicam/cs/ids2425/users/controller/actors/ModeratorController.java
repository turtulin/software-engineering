package it.unicam.cs.ids2425.users.controller.actors;

import it.unicam.cs.ids2425.articles.controller.ArticleController;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.users.controller.CanLogoutController;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.actors.Moderator;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.statuses.StatusInfo;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.wrappers.Pair;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
public class ModeratorController extends GenericUserController implements CanRegisterController, CanLogoutController {
    private final ArticleController articleController = SingletonController.getInstance(new ArticleController() {
    });

    public Pair<IArticle, List<StatusInfo<ArticleStatus>>> approve(@NonNull IArticle a, @NonNull Moderator m) {
        a = articleController.get(a, ArticleStatus.PENDING);
        IUser u = super.get(m, UserStatus.ACTIVE);
        if (u.getRole() != UserRole.MODERATOR) {
            throw new IllegalArgumentException("Moderator not found");
        }
        m = (Moderator) u;

        a = articleController.approveArticle(a, m);
        return articleController.getArticleStatus(a);
    }

    public Pair<IArticle, List<StatusInfo<ArticleStatus>>> reject(@NonNull IArticle a, @NonNull String reason, @NonNull Moderator m) {
        if (reason.isBlank()) {
            throw new IllegalArgumentException("Must provide a Reason");
        }
        a = articleController.get(a, ArticleStatus.PENDING);
        IUser u = super.get(m, UserStatus.ACTIVE);
        if (u.getRole() != UserRole.MODERATOR) {
            throw new IllegalArgumentException("Moderator not found");
        }
        m = (Moderator) u;

        a = articleController.rejectArticle(a, reason, m);
        return articleController.getArticleStatus(a);
    }

    public List<IArticle> getApprovedArticles(@NonNull Moderator m) {
        check(m, UserStatus.ACTIVE);
        return articleController.getAll(m, ArticleStatus.PUBLISHED);
    }

    public List<IArticle> getRejectedArticles(@NonNull Moderator m) {
        check(m, UserStatus.ACTIVE);
        return articleController.getAll(m, ArticleStatus.REJECTED);
    }

    public List<IArticle> getPendingArticles(IUser u) {
        check(u, UserStatus.ACTIVE);
        return articleController.getAll(u, ArticleStatus.PENDING);
    }

    @Override
    public IUser register(IUser u) {
        if (super.get(u, null) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        if (u.getRole() != UserRole.MODERATOR) {
            throw new IllegalArgumentException("User role is not valid");
        }
        Moderator m = (Moderator) u;
        userRepository.create(m);
        userStatusRepository.create(new Pair<>(m,
                List.of(statusInfoController.create(new StatusInfo<>(UserStatus.PENDING, m), m))));
        return super.get(m, UserStatus.PENDING);
    }

    @Override
    protected boolean check(@NonNull IUser u, UserStatus status) {
        IUser user = super.get(u, null);
        if (!super.check(u, status) && List.of(UserRole.MODERATOR, UserRole.ADMIN).contains(user.getRole())) {
            throw new IllegalArgumentException("User is not a moderator");
        }
        return true;
    }
}
