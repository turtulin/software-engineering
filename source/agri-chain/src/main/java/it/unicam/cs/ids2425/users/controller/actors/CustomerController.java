package it.unicam.cs.ids2425.users.controller.actors;

import it.unicam.cs.ids2425.articles.controller.ArticleController;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.controller.OrderController;
import it.unicam.cs.ids2425.eshop.controller.ReviewController;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.CartController;
import it.unicam.cs.ids2425.eshop.model.order.Order;
import it.unicam.cs.ids2425.eshop.model.order.OrderState;
import it.unicam.cs.ids2425.eshop.model.reviews.Review;
import it.unicam.cs.ids2425.eshop.model.stocks.Cart;
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
import java.util.NoSuchElementException;

public class CustomerController extends GenericUserController implements CanRegisterController, CanLogoutController {
    private final ArticleController articleController = SingletonController.getInstance(new ArticleController());
    private final CartController cartController = SingletonController.getInstance(new CartController());
    private final OrderController orderController = SingletonController.getInstance(new OrderController());
    private final ReviewController reviewController = SingletonController.getInstance(new ReviewController());

    public Cart addArticlesToCart(@NonNull IArticle article, @NonNull IUser user, @NonNull Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        user = super.get(user, UserStatus.ACTIVE);
        article = articleController.get(article, ArticleStatus.PUBLISHED);
        return cartController.addArticleToStock(article, (Customer) user, quantity);
    }

    public Cart removeArticlesFromCart(@NonNull IArticle article, @NonNull IUser user, @NonNull Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        user = super.get(user, UserStatus.ACTIVE);
        article = articleController.get(article, ArticleStatus.PUBLISHED);
        return cartController.removeArticleFromStock(article, (Customer) user, quantity);
    }

    public Order checkout(@NonNull Cart cart, @NonNull Address shippingAddress, @NonNull Address billingAddress, @NonNull IPaymentMethod payment, @NonNull IUser user) {
        user = super.get(user, UserStatus.ACTIVE);
        cart = cartController.get(cart);
        if (!cart.getUser().equals(user)) {
            throw new IllegalArgumentException("Cart does not belong to user");
        }
        Cart clone = (Cart) cart.clone();
        cartController.empty(cart);
        return orderController.create(clone, shippingAddress, billingAddress, payment);
    }

    public Review addReview(@NonNull IArticle article, @NonNull Review review, @NonNull IUser user) {
        user = super.get(user, UserStatus.ACTIVE);
        article = articleController.get(article, ArticleStatus.PUBLISHED);
        return reviewController.create(article, review, user);
    }

    public OrderState cancelOrder(@NonNull Order order, @NonNull IUser user) {
        user = super.get(user, UserStatus.ACTIVE);
        if (!order.getCart().getUser().equals(user)) {
            throw new IllegalArgumentException("Order does not belong to user");
        }
        return orderController.cancel(order);
    }

    @Override
    public IUser register(@NonNull IUser user) {
        try {
            super.get(user, null);
            throw new IllegalArgumentException("User already exists");
        } catch (NoSuchElementException ignored) {
        }

        if (user.getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("User role is not set");
        }
        Customer c = (Customer) user;
        userRepository.save(c);
        Cart cart = cartController.create(c);
        c.setStock(cart);
        UserState userStatus = new UserState(c, UserStatus.ACTIVE, null);

        userRepository.save(c);
        userStatusRepository.save(userStatus);

        return super.get(c, UserStatus.ACTIVE);
    }

    @Override
    protected boolean check(@NonNull IUser user, UserStatus status) {
        user = super.get(user, null);
        if (super.check(user, status) && List.of(UserRole.CUSTOMER, UserRole.CUSTOMER_SERVICE, UserRole.ADMIN).contains(user.getRole())) {
            return true;
        }
        throw new IllegalArgumentException("User is not a customer");
    }
}
