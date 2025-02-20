package it.unicam.cs.ids2425.users.controller.actors;

import it.unicam.cs.ids2425.articles.controller.ArticleController;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.users.controller.CanLogoutController;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.UserState;
import it.unicam.cs.ids2425.users.model.actors.Moderator;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.NoSuchElementException;

@NoArgsConstructor
public class ModeratorController extends GenericUserController implements CanRegisterController, CanLogoutController {
    private final ArticleController articleController = SingletonController.getInstance(new ArticleController());

    public ArticleStatus approve(@NonNull IArticle article, @NonNull Moderator moderator) {
        article = articleController.get(article, ArticleStatus.PENDING);
        IUser u = super.get(moderator, UserStatus.ACTIVE);
        if (u.getRole() != UserRole.MODERATOR) {
            throw new IllegalArgumentException("Moderator not found");
        }
        moderator = (Moderator) u;

        article = articleController.approveArticle(article, moderator);
        return articleController.getArticleStatus(article);
    }

    public ArticleStatus reject(@NonNull IArticle article, @NonNull String reason, @NonNull Moderator moderator) {
        if (reason.isBlank()) {
            throw new IllegalArgumentException("Must provide a Reason");
        }
        article = articleController.get(article, ArticleStatus.PENDING);
        IUser u = super.get(moderator, UserStatus.ACTIVE);
        if (u.getRole() != UserRole.MODERATOR) {
            throw new IllegalArgumentException("Moderator not found");
        }
        moderator = (Moderator) u;

        article = articleController.rejectArticle(article, reason, moderator);
        return articleController.getArticleStatus(article);
    }

    public List<IArticle> getApprovedArticles(@NonNull Moderator moderator) {
        check(moderator, UserStatus.ACTIVE);
        return articleController.getAll(moderator, ArticleStatus.PUBLISHED);
    }

    public List<IArticle> getRejectedArticles(@NonNull Moderator moderator) {
        check(moderator, UserStatus.ACTIVE);
        return articleController.getAll(moderator, ArticleStatus.REJECTED);
    }

    public List<IArticle> getPendingArticles(@NonNull Moderator moderator) {
        check(moderator, UserStatus.ACTIVE);
        return articleController.getAll(moderator, ArticleStatus.PENDING);
    }

    @Override
    public IUser register(IUser u) {
        try {
            super.get(u, null);
            throw new IllegalArgumentException("User already exists");
        } catch (NoSuchElementException ignored) {
        }
        if (u.getRole() != UserRole.MODERATOR) {
            throw new IllegalArgumentException("User role is not valid");
        }
        Moderator m = (Moderator) u;
        UserState userStatus = new UserState(m, UserStatus.PENDING, null);

        userRepository.save(m);
        userStatusRepository.save(userStatus);
        return super.get(m, UserStatus.PENDING);
    }

    @Override
    protected boolean check(@NonNull IUser user, UserStatus status) {
        IUser u = super.get(user, null);
        if (!super.check(user, status) && List.of(UserRole.MODERATOR, UserRole.ADMIN).contains(u.getRole())) {
            throw new IllegalArgumentException("User is not a moderator");
        }
        return true;
    }
}
