package it.unicam.cs.ids2425.view;

import com.fasterxml.jackson.databind.JsonNode;
import it.unicam.cs.ids2425.controller.article.actor.GuestArticleController;
import it.unicam.cs.ids2425.model.article.Article;
import it.unicam.cs.ids2425.model.authentication.Token;
import it.unicam.cs.ids2425.model.eshop.review.Review;
import it.unicam.cs.ids2425.controller.user.OtherUserController;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class GuestView implements IView {
    private final GuestArticleController guestController;
    private final OtherUserController userController;

    @Autowired
    public GuestView(GuestArticleController guestController, OtherUserController userController) {
        this.guestController = guestController;
        this.userController = userController;
    }

    @RequestMapping(path = "/login", method = {RequestMethod.POST})
    public ResponseEntity<ViewResponse<Token>> login(@RequestBody JsonNode body) {
        String username = body.get("username").asText();
        String password = body.get("password").asText();
        return genericCall(() -> userController.login(username, password));
    }

    @RequestMapping(path = "/article/all", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<Article>>> getAllArticles() {
        return genericCall(() -> guestController.getAllArticles(ArticleStatusCode.PUBLISHED));
    }

    @RequestMapping(path = "/article/{id}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<Article>> getArticleById(@PathVariable Long id) {
        return genericCall(() -> guestController.getArticleById(id, ArticleStatusCode.PUBLISHED));
    }

    @RequestMapping(path = "/article/{id}/review/all", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<Review>>> getArticleReviewsById(@PathVariable Long id) {
        return genericCall(() -> guestController.getArticleReviewsById(id, ArticleStatusCode.PUBLISHED));
    }

    @RequestMapping(path = "/article/{id}/share", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<Map<String, String>>> shareArticle(@PathVariable Long id) {
        return genericCall(() -> guestController.shareArticle(id, ArticleStatusCode.PUBLISHED));
    }
}
