package it.unicam.cs.ids2425.user.view;

import it.unicam.cs.ids2425.article.controller.actor.SellerArticleController;
import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.model.ArticleState;
import it.unicam.cs.ids2425.problem.controller.ProblemController;
import it.unicam.cs.ids2425.user.controller.actor.OtherUserController;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Getter
public abstract class SellerView implements IView, ICanLogoutView, ICanRegisterView, ICanReportView {
    private final OtherUserController logoutController;
    private final OtherUserController registerController;
    private final ProblemController problemController;

    private final SellerArticleController sellerArticleController;

    public SellerView(OtherUserController logoutController, SellerArticleController sellerArticleController, ProblemController problemController) {
        this.logoutController = logoutController;
        this.registerController = logoutController;
        this.sellerArticleController = sellerArticleController;
        this.problemController = problemController;
    }

    @RequestMapping(value = "/article/all", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<Article>>> getAllArticles(@RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.getAllArticles(user));
    }

    @RequestMapping(value = "/article/all/{status}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<Article>>> getAllArticles(@PathVariable String status,
                                                                      @RequestAttribute("user") User user) {
        ArticleStatusCode statusCode = ArticleStatusCode.valueOf(status.toUpperCase());
        return genericCall(() -> sellerArticleController.getAllArticles(statusCode, user));
    }

    @RequestMapping(value = "/article/{articleId}/{status}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<Article>> getArticle(@PathVariable Long articleId,
                                                            @PathVariable String status,
                                                            @RequestAttribute("user") User user) {
        ArticleStatusCode statusCode = ArticleStatusCode.valueOf(status.toUpperCase());
        return genericCall(() -> sellerArticleController.getArticle(articleId, statusCode, user));
    }

    @RequestMapping(value = "/article", method = {RequestMethod.POST})
    public ResponseEntity<ViewResponse<Article>> createArticle(@RequestBody Article article,
                                                               @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.createArticle(article, user));
    }

    @RequestMapping(value = "/article", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Article>> updateArticle(@RequestBody Article article,
                                                               @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.updateArticle(article, user));
    }

    @RequestMapping(value = "/article/{articleId}", method = {RequestMethod.DELETE})
    public ResponseEntity<ViewResponse<ArticleState>> deleteArticle(@PathVariable Long articleId,
                                                                    @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.updateArticle(articleId, ArticleStatusCode.DELETED, user));
    }

    @RequestMapping(value = "/article/{articleId}/draft", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<ArticleState>> draftArticle(@PathVariable Long articleId,
                                                                   @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.updateArticle(articleId, ArticleStatusCode.DRAFT, user));
    }

    @RequestMapping(value = "/article/{articleId}/publish", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<ArticleState>> publishArticle(@PathVariable Long articleId,
                                                                     @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.updateArticle(articleId, ArticleStatusCode.PENDING, user));
    }

    @RequestMapping(value = "/article/{articleId}/quantity/{quantity}", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Article>> updateArticleQuantity(@PathVariable Long articleId,
                                                                       @PathVariable Long quantity,
                                                                       @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.updateArticleQuantity(articleId, quantity, user));
    }

    @RequestMapping(value = "/article/{articleId}/quantity", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<Long>> getArticleQuantity(@PathVariable Long articleId,
                                                                 @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.getArticleQuantity(articleId, user));
    }
}
