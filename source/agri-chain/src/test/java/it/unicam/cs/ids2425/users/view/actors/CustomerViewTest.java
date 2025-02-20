package it.unicam.cs.ids2425.users.view.actors;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.model.order.Order;
import it.unicam.cs.ids2425.eshop.model.order.OrderState;
import it.unicam.cs.ids2425.eshop.model.reviews.Review;
import it.unicam.cs.ids2425.eshop.model.reviews.ReviewRatings;
import it.unicam.cs.ids2425.eshop.model.stocks.Cart;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.Customer;
import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.payments.PayPal;
import it.unicam.cs.ids2425.users.view.GenericUserViewTest;
import it.unicam.cs.ids2425.utilities.statuses.OrderStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomerViewTest extends GenericUserViewTest {
    @Test
    void register() {
        Customer newCustomer = new Customer("newCustomer", "password");
        ViewResponse<IUser> response = customerView.register(newCustomer);
        assertAll(
                () -> assertEquals(ResponseStatus.CREATED, response.getStatus()),
                () -> assertNotNull(response.getData().getId()),
                () -> assertEquals("newCustomer", response.getData().getUsername())
        );
    }

    @Test
    void addArticleToCart() {
        assertNotNull(customer.getStock());
        assertNotNull(customer.getStock().getArticles());
        assertTrue(customer.getStock().getArticles().isEmpty());

        List<IArticle> data = addArticles(1, 2);

        IArticle article = data.getFirst();
        customerView.addArticleToCart(article, customer, 1);
        Map<IArticle, Integer> articles = customer.getStock().getArticles();
        assertEquals(1, articles.size());
        assertEquals(1, articles.get(article));

        customerView.addArticleToCart(article, customer, 1);
        Map<IArticle, Integer> articles2 = customer.getStock().getArticles();
        assertEquals(1, articles2.size());
        assertEquals(2, articles2.get(article));
    }

    @Test
    void removeArticleFromCart() {
        List<IArticle> data = addArticles(1, 2);
        IArticle article = data.getFirst();
        customerView.addArticleToCart(article, customer, 2);
        assertEquals(2, customer.getStock().getArticles().get(article));

        ViewResponse<Cart> cartViewResponse = customerView.removeArticleFromCart(article, customer, 1);
        assertEquals(ResponseStatus.OK, cartViewResponse.getStatus());
        assertEquals(customer.getStock(), cartViewResponse.getData());
        assertNull(cartViewResponse.getMessage());
        assertEquals(1, customer.getStock().getArticles().get(article));

        cartViewResponse = customerView.removeArticleFromCart(article, customer, 1);
        assertEquals(ResponseStatus.OK, cartViewResponse.getStatus());
        assertEquals(customer.getStock(), cartViewResponse.getData());
        assertNull(cartViewResponse.getMessage());
        assertTrue(customer.getStock().getArticles().isEmpty());

        cartViewResponse = customerView.removeArticleFromCart(article, customer, 1);
        assertEquals(ResponseStatus.BAD_REQUEST, cartViewResponse.getStatus());
        assertNull(cartViewResponse.getData());
        assertEquals("Article Problem", cartViewResponse.getMessage());
        assertTrue(customer.getStock().getArticles().isEmpty());
    }

    @Test
    void checkout() {
        ViewResponse<Order> orderViewResponse = customerView.checkout(null, null, null, null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, orderViewResponse.getStatus());
        assertNull(orderViewResponse.getData());
        assertEquals("cart is marked non-null but is null", orderViewResponse.getMessage());

        orderViewResponse = customerView.checkout(customer.getStock(), null, null, null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, orderViewResponse.getStatus());
        assertNull(orderViewResponse.getData());
        assertEquals("shippingAddress is marked non-null but is null", orderViewResponse.getMessage());

        orderViewResponse = customerView.checkout(customer.getStock(),
                new Address(123.0, 465.0, "", "", "", "", ""),
                null, null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, orderViewResponse.getStatus());
        assertNull(orderViewResponse.getData());
        assertEquals("billingAddress is marked non-null but is null", orderViewResponse.getMessage());

        orderViewResponse = customerView.checkout(customer.getStock(),
                new Address(123.0, 465.0, "", "", "", "", ""),
                new Address(123.0, 465.0, "", "", "", "", ""),
                null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, orderViewResponse.getStatus());
        assertNull(orderViewResponse.getData());
        assertEquals("payment is marked non-null but is null", orderViewResponse.getMessage());

        orderViewResponse = customerView.checkout(customer.getStock(),
                new Address(123.0, 465.0, "", "", "", "", ""),
                new Address(123.0, 465.0, "", "", "", "", ""),
                new PayPal("mockPayPal", "mockPassword"), null);
        assertEquals(ResponseStatus.BAD_REQUEST, orderViewResponse.getStatus());
        assertNull(orderViewResponse.getData());
        assertEquals("user is marked non-null but is null", orderViewResponse.getMessage());

        List<IArticle> data = addArticles(1, 2);
        IArticle article = data.getFirst();
        customerView.addArticleToCart(article, customer, 2);
        Cart cart = customer.getStock();
        assertEquals(2, customer.getStock().getArticles().get(article));
        orderViewResponse = customerView.checkout(customer.getStock(),
                new Address(123.0, 465.0, "", "", "", "", ""),
                new Address(123.0, 465.0, "", "", "", "", ""),
                new PayPal("mockPayPal", "mockPassword"), customer);
        assertEquals(ResponseStatus.CREATED, orderViewResponse.getStatus());
        assertNotNull(orderViewResponse.getData());
        assertNull(orderViewResponse.getMessage());
        assertTrue(customer.getStock().getArticles().isEmpty());
        assertEquals(cart, orderViewResponse.getData().getCart());
    }

    @Test
    void cancelOrder() {
        ViewResponse<OrderState> orderStateViewResponse = customerView.cancelOrder(null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, orderStateViewResponse.getStatus());
        assertNull(orderStateViewResponse.getData());
        assertEquals("order is marked non-null but is null", orderStateViewResponse.getMessage());

        List<IArticle> data = addArticles(1, 2);
        IArticle article = data.getFirst();
        customerView.addArticleToCart(article, customer, 2);
        ViewResponse<Order> orderViewResponse = customerView.checkout(customer.getStock(),
                new Address(123.0, 465.0, "", "", "", "", ""),
                new Address(123.0, 465.0, "", "", "", "", ""),
                new PayPal("mockPayPal", "mockPassword"), customer);
        Order order = orderViewResponse.getData();

        orderStateViewResponse = customerView.cancelOrder(order, null);
        assertEquals(ResponseStatus.BAD_REQUEST, orderStateViewResponse.getStatus());
        assertNull(orderStateViewResponse.getData());
        assertEquals("user is marked non-null but is null", orderStateViewResponse.getMessage());

        IUser fakeUser = addUsers(1).getFirst();
        orderStateViewResponse = customerView.cancelOrder(order, fakeUser);
        assertEquals(ResponseStatus.BAD_REQUEST, orderStateViewResponse.getStatus());
        assertNull(orderStateViewResponse.getData());
        assertEquals("Order does not belong to user", orderStateViewResponse.getMessage());

        ViewResponse<OrderState> orderStateViewResponse1 = customerView.cancelOrder(order, customer);
        assertEquals(ResponseStatus.OK, orderStateViewResponse1.getStatus());
        assertNotNull(orderStateViewResponse1.getData());
        assertEquals(OrderStatus.CANCELLED, orderStateViewResponse1.getData().getStatus());
        assertEquals(order, orderStateViewResponse1.getData().getEntity());
        assertEquals(customer, orderStateViewResponse1.getData().getInitiator());
        assertNull(orderStateViewResponse1.getMessage());
    }

    @Test
    void addReview() {
        ViewResponse<Review> reviewViewResponse = customerView.addReview(null, null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, reviewViewResponse.getStatus());
        assertNull(reviewViewResponse.getData());
        assertEquals("article is marked non-null but is null", reviewViewResponse.getMessage());

        IArticle article = addArticles(1, 1).getFirst();
        reviewViewResponse = customerView.addReview(article, null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, reviewViewResponse.getStatus());
        assertNull(reviewViewResponse.getData());
        assertEquals("review is marked non-null but is null", reviewViewResponse.getMessage());

        Review review = new Review(customer, article, ReviewRatings.OK, "mockReview", "mockComment");
        reviewViewResponse = customerView.addReview(article, review, null);
        assertEquals(ResponseStatus.BAD_REQUEST, reviewViewResponse.getStatus());
        assertNull(reviewViewResponse.getData());
        assertEquals("user is marked non-null but is null", reviewViewResponse.getMessage());

        reviewViewResponse = customerView.addReview(article, review, customer);
        assertEquals(ResponseStatus.CREATED, reviewViewResponse.getStatus());
        assertNotNull(reviewViewResponse.getData());
        assertEquals(review, reviewViewResponse.getData());
        assertNull(reviewViewResponse.getMessage());
    }
}