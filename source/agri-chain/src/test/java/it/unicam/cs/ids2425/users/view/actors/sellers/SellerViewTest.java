package it.unicam.cs.ids2425.users.view.actors.sellers;

import it.unicam.cs.ids2425.articles.model.articles.Event;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.model.order.Order;
import it.unicam.cs.ids2425.eshop.model.stocks.Cart;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.sellers.EventPlanner;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.payments.PayPal;
import it.unicam.cs.ids2425.users.view.GenericUserViewTest;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SellerViewTest extends GenericUserViewTest {
    @Test
    void register() {
        ISeller newSeller = new EventPlanner("newSeller", "password");
        ViewResponse<IUser> response = sellerView.register(newSeller);
        assertAll(
                () -> assertEquals(ResponseStatus.CREATED, response.getStatus()),
                () -> assertNotNull(response.getData().getId()),
                () -> assertEquals("newSeller", response.getData().getUsername())
        );
        assertNotNull(adminView.getUser(admin, response.getData(), UserStatus.PENDING).getData());
    }

    @Test
    void getAll() {
        ViewResponse<List<IArticle>> articles = sellerView.getAll(null);
        assertNotNull(articles);
        assertEquals(ResponseStatus.BAD_REQUEST, articles.getStatus());
        assertNull(articles.getData());
        assertEquals("user is marked non-null but is null", articles.getMessage());

        articles = sellerView.getAll(seller);
        assertNotNull(articles);
        assertEquals(ResponseStatus.OK, articles.getStatus());
        assertNull(articles.getMessage());
        assertNotNull(articles.getData());
        assertTrue(articles.getData().isEmpty());

        IArticle article = addArticles(1, 1).getFirst();
        sellerView.create(article, seller);
        articles = sellerView.getAll(seller);
        assertNotNull(articles);
        assertEquals(ResponseStatus.OK, articles.getStatus());
        assertNull(articles.getMessage());
        assertNotNull(articles.getData());
        assertFalse(articles.getData().isEmpty());
        assertEquals(articles.getData().stream().filter(a -> a.getSeller().equals(seller)).toList(), articles.getData());
    }

    @Test
    void create() {
        ViewResponse<IArticle> articleViewResponse = sellerView.create(null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, articleViewResponse.getStatus());
        assertNull(articleViewResponse.getData());
        assertEquals("article is marked non-null but is null", articleViewResponse.getMessage());

        IArticle article = new Event("event", "description", seller, 10);
        articleViewResponse = sellerView.create(article, null);
        assertEquals(ResponseStatus.BAD_REQUEST, articleViewResponse.getStatus());
        assertNull(articleViewResponse.getData());
        assertEquals("seller is marked non-null but is null", articleViewResponse.getMessage());

        articleViewResponse = sellerView.create(article, seller);
        assertEquals(ResponseStatus.CREATED, articleViewResponse.getStatus());
        assertNotNull(articleViewResponse.getData());
        assertEquals(article, articleViewResponse.getData());

        assertNotNull(sellerView.get(articleViewResponse.getData(), ArticleStatus.DRAFT).getData());
    }

    @Test
    void updateArticle() {
        ViewResponse<IArticle> articleViewResponse = sellerView.updateArticle(null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, articleViewResponse.getStatus());
        assertNull(articleViewResponse.getData());
        assertEquals("article is marked non-null but is null", articleViewResponse.getMessage());

        Event article = (Event) addArticles(1, 1).getFirst();
        article.setDescription("new description");

        articleViewResponse = sellerView.updateArticle(article, null);
        assertEquals(ResponseStatus.BAD_REQUEST, articleViewResponse.getStatus());
        assertNull(articleViewResponse.getData());
        assertEquals("seller is marked non-null but is null", articleViewResponse.getMessage());

        articleViewResponse = sellerView.updateArticle(article, seller);
        assertEquals(ResponseStatus.ACCEPTED, articleViewResponse.getStatus());
        assertNotNull(articleViewResponse.getData());
        assertEquals(article, articleViewResponse.getData());

        assertNotNull(sellerView.get(articleViewResponse.getData(), ArticleStatus.DRAFT).getData());
    }

    @Test
    void deleteArticle() {
        ViewResponse<IArticle> articleViewResponse = sellerView.updateArticle(null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, articleViewResponse.getStatus());
        assertNull(articleViewResponse.getData());
        assertEquals("article is marked non-null but is null", articleViewResponse.getMessage());

        Event article = (Event) addArticles(1, 1).getFirst();

        articleViewResponse = sellerView.deleteArticle(article, null);
        assertEquals(ResponseStatus.BAD_REQUEST, articleViewResponse.getStatus());
        assertNull(articleViewResponse.getData());
        assertEquals("seller is marked non-null but is null", articleViewResponse.getMessage());

        articleViewResponse = sellerView.deleteArticle(article, seller);
        assertEquals(ResponseStatus.ACCEPTED, articleViewResponse.getStatus());
        assertNotNull(articleViewResponse.getData());
        assertEquals(article, articleViewResponse.getData());

        assertNotNull(sellerView.get(articleViewResponse.getData(), ArticleStatus.DELETED).getData());
    }

    @Test
    void publishArticle() {
        ViewResponse<IArticle> articleViewResponse = sellerView.publishArticle(null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, articleViewResponse.getStatus());
        assertNull(articleViewResponse.getData());
        assertEquals("article is marked non-null but is null", articleViewResponse.getMessage());

        Event article = (Event) addArticles(1, 1).getFirst();

        articleViewResponse = sellerView.publishArticle(article, null);
        assertEquals(ResponseStatus.BAD_REQUEST, articleViewResponse.getStatus());
        assertNull(articleViewResponse.getData());
        assertEquals("seller is marked non-null but is null", articleViewResponse.getMessage());

        articleViewResponse = sellerView.publishArticle(article, seller);
        assertEquals(ResponseStatus.ACCEPTED, articleViewResponse.getStatus());
        assertNotNull(articleViewResponse.getData());
        assertEquals(article, articleViewResponse.getData());

        assertNotNull(sellerView.get(articleViewResponse.getData(), ArticleStatus.PENDING).getData());
    }

    @Test
    void draftArticle() {
        ViewResponse<IArticle> articleViewResponse = sellerView.draftArticle(null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, articleViewResponse.getStatus());
        assertNull(articleViewResponse.getData());
        assertEquals("article is marked non-null but is null", articleViewResponse.getMessage());

        Event article = (Event) addArticles(1, 1).getFirst();

        articleViewResponse = sellerView.draftArticle(article, null);
        assertEquals(ResponseStatus.BAD_REQUEST, articleViewResponse.getStatus());
        assertNull(articleViewResponse.getData());
        assertEquals("seller is marked non-null but is null", articleViewResponse.getMessage());

        articleViewResponse = sellerView.draftArticle(article, seller);
        assertEquals(ResponseStatus.ACCEPTED, articleViewResponse.getStatus());
        assertNotNull(articleViewResponse.getData());
        assertEquals(article, articleViewResponse.getData());

        assertNotNull(sellerView.get(articleViewResponse.getData(), ArticleStatus.DRAFT).getData());
    }

    @Test
    void setArticleQuantityTest() {
        ViewResponse<IArticle> response = sellerView.setArticleQuantity(null, null, -1);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("article is marked non-null but is null", response.getMessage());

        IArticle article = addArticles(1, 0).getFirst();
        response = sellerView.setArticleQuantity(article, null, -1);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("seller is marked non-null but is null", response.getMessage());

        response = sellerView.setArticleQuantity(article, seller, -1);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("Quantity must be positive", response.getMessage());

        response = sellerView.setArticleQuantity(article, seller, 0);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("Quantity must be positive", response.getMessage());

        response = sellerView.setArticleQuantity(article, seller, 1);
        assertEquals(ResponseStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(article, response.getData());
        Cart c = customerView.addArticleToCart(article, customer, 1).getData();
        assertEquals(1, c.getArticles().get(article));
        Order o = customerView.checkout(c,
                new Address(123, 456, "notes", "street", "city", "zip", "country"),
                new Address(123, 456, "notes", "street", "city", "zip", "country"),
                new PayPal("email", "password"),
                customer).getData();
        assertEquals(1, o.getCart().getArticles().get(article));
        ViewResponse<Integer> articleQuantityViewResponse = sellerView.getArticleQuantity(article, seller);
        assertEquals(ResponseStatus.OK, articleQuantityViewResponse.getStatus());
        assertEquals(0, articleQuantityViewResponse.getData());
        assertNull(articleQuantityViewResponse.getMessage());
    }
}
