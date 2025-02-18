package it.unicam.cs.ids2425.users.controller.actors;

import it.unicam.cs.ids2425.articles.controller.ArticleController;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.controller.CartController;
import it.unicam.cs.ids2425.eshop.controller.OrderController;
import it.unicam.cs.ids2425.eshop.controller.ReviewController;
import it.unicam.cs.ids2425.eshop.model.Cart;
import it.unicam.cs.ids2425.eshop.model.order.Order;
import it.unicam.cs.ids2425.eshop.model.order.OrderState;
import it.unicam.cs.ids2425.eshop.model.reviews.Review;
import it.unicam.cs.ids2425.users.controller.CanLogoutController;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.UserState;
import it.unicam.cs.ids2425.users.model.actors.Customer;
import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import lombok.NonNull;

import java.util.List;

public class CustomerController extends GenericUserController implements CanRegisterController, CanLogoutController {
    private final ArticleController articleController = SingletonController.getInstance(new ArticleController() {
    });
    private final CartController cartController = SingletonController.getInstance(new CartController() {
    });
    private final OrderController orderController = SingletonController.getInstance(new OrderController() {
    });
    private final ReviewController reviewController = SingletonController.getInstance(new ReviewController() {
    });

    public Cart addArticlesToCart(@NonNull IArticle article, @NonNull IUser user) {
        user = super.get(user, UserStatus.ACTIVE);
        article = articleController.get(article, ArticleStatus.PUBLISHED);
        return cartController.addArticleToCart(article, user);
    }

    public Cart removeArticlesFromCart(@NonNull IArticle article, @NonNull IUser user) {
        user = super.get(user, UserStatus.ACTIVE);
        article = articleController.get(article, ArticleStatus.PUBLISHED);
        return cartController.removeArticleFromCart(article, user);
    }

    public Order checkout(@NonNull Cart cart, @NonNull Address shippingAddress, @NonNull Address billingAddress, @NonNull IPaymentMethod payment) {
        cart = cartController.get(cart);
        return orderController.create(cart, shippingAddress, billingAddress, payment);
    }

    public Review addReview(@NonNull IArticle article, @NonNull Review review, @NonNull IUser user) {
        user = super.get(user, UserStatus.ACTIVE);
        article = articleController.get(article, ArticleStatus.PUBLISHED);
        return reviewController.create(article, review, user);
    }

    public OrderState cancelOrder(@NonNull Order order) {
        return orderController.cancel(order);
    }

    public OrderState returnOrder(@NonNull Order order) {
        return orderController.returnOrder(order);
    }

    @Override
    public IUser register(@NonNull IUser u) {
        if (super.get(u, null) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        if (u.getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("User role is not set");
        }
        Customer c = (Customer) u;
        if (c.getCart() == null) {
            c.setCart(new Cart(c));
        }
        UserState userStatus = new UserState(c, UserStatus.ACTIVE, null);

        cartController.create(c);
        userRepository.save(c);
        userStatusRepository.save(userStatus);

        return super.get(c, UserStatus.ACTIVE);
    }

    @Override
    protected boolean check(@NonNull IUser u, UserStatus status) {
        u = super.get(u, null);
        if (super.check(u, status) && List.of(UserRole.CUSTOMER, UserRole.CUSTOMER_SERVICE, UserRole.ADMIN).contains(u.getRole())) {
            return true;
        }
        throw new IllegalArgumentException("User is not a customer");
    }
}
