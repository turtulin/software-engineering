package it.unicam.cs.ids2425.users.view.actors;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.actors.ModeratorController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.Moderator;
import it.unicam.cs.ids2425.users.view.CanRegisterView;
import it.unicam.cs.ids2425.users.view.CanReportView;
import it.unicam.cs.ids2425.users.view.GenericUserView;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.statuses.StatusInfo;
import it.unicam.cs.ids2425.utilities.wrappers.Pair;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ModeratorView extends GenericUserView implements CanRegisterView, CanReportView {
    private final ModeratorController moderatorController = SingletonController.getInstance(new ModeratorController() {
    });

    public ViewResponse<Pair<IArticle, List<StatusInfo<ArticleStatus>>>> approve(IArticle article, IUser user) {
        return genericCall(() -> moderatorController.approve(article, (Moderator) user),
                ResponseStatus.ACCEPTED,
                "Article approved");
    }

    public ViewResponse<Pair<IArticle, List<StatusInfo<ArticleStatus>>>> reject(IArticle article, String reason, IUser user) {
        return genericCall(() -> moderatorController.reject(article, reason, (Moderator) user),
                ResponseStatus.ACCEPTED,
                "Article rejected");
    }

    public ViewResponse<List<IArticle>> getApprovedArticles(IUser user) {
        return genericCall(() -> moderatorController.getApprovedArticles((Moderator) user));
    }

    public ViewResponse<List<IArticle>> getRejectedArticles(IUser user) {
        return genericCall(() -> moderatorController.getRejectedArticles((Moderator) user));
    }

    public ViewResponse<List<IArticle>> getPendingArticles(IUser user) {
        return genericCall(() -> moderatorController.getPendingArticles(user));
    }

    @Override
    public CanRegisterController getCanRegisterController() {
        return moderatorController;
    }
}
