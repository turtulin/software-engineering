package it.unicam.cs.ids2425.view;

import it.unicam.cs.ids2425.controller.article.actor.SellerArticleController;
import it.unicam.cs.ids2425.model.article.Article;
import it.unicam.cs.ids2425.model.article.ArticleState;
import it.unicam.cs.ids2425.controller.problem.ProblemController;
import it.unicam.cs.ids2425.controller.user.OtherUserController;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Getter
public abstract class SellerView<T extends Article> implements IView, ICanLogoutView, ICanRegisterView, ICanReportView {
    private final OtherUserController logoutController;
    private final OtherUserController registerController;
    private final ProblemController problemController;

    private final SellerArticleController<T> sellerArticleController;

    public SellerView(OtherUserController logoutController, SellerArticleController<T> sellerArticleController, ProblemController problemController) {
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

    @RequestMapping(value = "/article/{id}/{status}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<Article>> getArticle(@PathVariable Long id,
                                                            @PathVariable String status,
                                                            @RequestAttribute("user") User user) {
        ArticleStatusCode statusCode = ArticleStatusCode.valueOf(status.toUpperCase());
        return genericCall(() -> sellerArticleController.getArticle(id, statusCode, user));
    }

    @RequestMapping(value = "/article", method = {RequestMethod.POST})
    public ResponseEntity<ViewResponse<Article>> createArticle(@RequestBody T article,
                                                               @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.createArticle(article, user));
    }

    @RequestMapping(value = "/article", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Article>> updateArticle(@RequestBody T article,
                                                               @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.updateArticle(article, user));
    }

    @RequestMapping(value = "/article/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity<ViewResponse<ArticleState>> deleteArticle(@PathVariable Long id,
                                                                    @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.updateArticle(id, ArticleStatusCode.DELETED, user));
    }

    @RequestMapping(value = "/article/{id}/draft", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<ArticleState>> draftArticle(@PathVariable Long id,
                                                                   @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.updateArticle(id, ArticleStatusCode.DRAFT, user));
    }

    @RequestMapping(value = "/article/{id}/publish", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<ArticleState>> publishArticle(@PathVariable Long id,
                                                                     @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.updateArticle(id, ArticleStatusCode.PENDING, user));
    }

    @RequestMapping(value = "/article/{id}/quantity/{quantity}", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<Article>> updateArticleQuantity(@PathVariable Long id,
                                                                       @PathVariable Long quantity,
                                                                       @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.updateArticleQuantity(id, quantity, user));
    }

    @RequestMapping(value = "/article/{id}/quantity", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<Long>> getArticleQuantity(@PathVariable Long id,
                                                                 @RequestAttribute("user") User user) {
        return genericCall(() -> sellerArticleController.getArticleQuantity(id, user));
    }
}
