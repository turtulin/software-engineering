package it.unicam.cs.ids2425.users.view.actors;

import it.unicam.cs.ids2425.articles.model.articles.Event;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.Moderator;
import it.unicam.cs.ids2425.users.view.GenericUserViewTest;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ModeratorViewTest extends GenericUserViewTest {
    @Test
    void register() {
        Moderator newModerator = new Moderator("newModerator", "password");
        ViewResponse<IUser> response = moderatorView.register(newModerator);
        assertEquals(ResponseStatus.CREATED, response.getStatus());
        assertNotNull(response.getData().getId());
    }

    @Test
    void approve() {
        updateArticle((a) -> moderatorView.approve(a, moderator), ArticleStatus.PUBLISHED);
    }

    @Test
    void reject() {
        updateArticle((article) -> moderatorView.reject(article, "Invalid content", moderator), ArticleStatus.REJECTED);
    }

    private void updateArticle(Function<IArticle, ViewResponse<ArticleStatus>> function, ArticleStatus finalStatus) {
        IArticle article = new Event("Test Event", "Description", seller, 50.0);
        ViewResponse<IArticle> createResponse = sellerView.create(article, seller);
        ViewResponse<IArticle> publishResponse = sellerView.publishArticle(createResponse.getData(), seller);
        ViewResponse<ArticleStatus> rejectResponse = function.apply(publishResponse.getData());
        assertEquals(ResponseStatus.ACCEPTED, rejectResponse.getStatus());
        assertEquals(finalStatus, rejectResponse.getData());
    }

    @Test
    void getApprovedArticles() {
        getArticles(moderatorView::getApprovedArticles);
    }

    @Test
    void getRejectedArticles() {
        getArticles(moderatorView::getRejectedArticles);
    }

    @Test
    void getPendingArticles() {
        getArticles(moderatorView::getPendingArticles);
    }

    private void getArticles(Function<Moderator, ViewResponse<List<IArticle>>> function) {
        ViewResponse<List<IArticle>> response = function.apply(null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("moderator is marked non-null but is null", response.getMessage());

        response = function.apply(moderator);
        assertEquals(ResponseStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertNull(response.getMessage());
    }
}