package it.unicam.cs.ids2425.user.view;

import it.unicam.cs.ids2425.article.controller.actor.ModeratorArticleController;
import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.problem.controller.ProblemController;
import it.unicam.cs.ids2425.user.controller.actor.OtherUserController;
import it.unicam.cs.ids2425.user.controller.actor.UserController;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moderator")
public class ModeratorView implements IView, ICanLogoutView, ICanRegisterView, ICanReportView {
    @Getter
    private final OtherUserController logoutController;
    @Getter
    private final UserController registerController;
    @Getter
    private final ProblemController problemController;

    private final ModeratorArticleController moderatorController;

    @Autowired
    public ModeratorView(OtherUserController logoutController,
                         ProblemController problemController,
                         ModeratorArticleController moderatorController) {
        this.logoutController = logoutController;
        this.registerController = logoutController;
        this.problemController = problemController;
        this.moderatorController = moderatorController;
    }

    @RequestMapping(value = "/{id}/approve", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Article>> approveArticle(@PathVariable Long id,
                                                                @RequestAttribute("user") User user) {
        return genericCall(() -> moderatorController.approveArticle(id, user));
    }

    @RequestMapping(value = "/{id}/reject", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Article>> rejectArticle(@PathVariable Long id,
                                                               @RequestAttribute("user") User user) {
        return genericCall(() -> moderatorController.rejectArticle(id, user));
    }

    @RequestMapping(value = "/all/approved", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<Article>>> getAllApprovedArticles(@RequestAttribute("user") User user) {
        return genericCall(() -> moderatorController.getAllArticles(ArticleStatusCode.PUBLISHED, user));
    }

    @RequestMapping(value = "/all/rejected", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<Article>>> getAllRejectedArticles(@RequestAttribute("user") User user) {
        return genericCall(() -> moderatorController.getAllArticles(ArticleStatusCode.REJECTED, user));
    }

    @RequestMapping(value = "/all/pending", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<Article>>> getAllPendingArticles(@RequestAttribute("user") User user) {
        return genericCall(() -> moderatorController.getAllArticles(ArticleStatusCode.PENDING, user));
    }
}
