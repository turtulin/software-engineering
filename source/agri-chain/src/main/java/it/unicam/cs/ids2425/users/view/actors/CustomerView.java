package it.unicam.cs.ids2425.users.view.actors;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.model.order.Order;
import it.unicam.cs.ids2425.eshop.model.order.OrderState;
import it.unicam.cs.ids2425.eshop.model.reviews.Review;
import it.unicam.cs.ids2425.eshop.model.stocks.Cart;
import it.unicam.cs.ids2425.users.controller.CanLoginController;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.actors.CustomerController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;
import it.unicam.cs.ids2425.users.view.CanLoginView;
import it.unicam.cs.ids2425.users.view.CanRegisterView;
import it.unicam.cs.ids2425.users.view.CanReportView;
import it.unicam.cs.ids2425.users.view.GenericUserView;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomerView extends GenericUserView implements CanRegisterView, CanLoginView, CanReportView {
    private final CustomerController customerController = SingletonController.getInstance(new CustomerController());

    public ViewResponse<Cart> addArticleToCart(IArticle article, IUser user, Integer quantity) {
        return genericCall(() -> customerController.addArticlesToCart(article, user, quantity));
    }

    public ViewResponse<Cart> removeArticleFromCart(IArticle article, IUser user, Integer quantity) {
        return genericCall(() -> customerController.removeArticlesFromCart(article, user, quantity));
    }

    public ViewResponse<Order> checkout(Cart cart, Address shippingAddress, Address billingAddress, IPaymentMethod payment, IUser user) {
        return genericCall(() -> customerController.checkout(cart, shippingAddress, billingAddress, payment, user),
                ResponseStatus.CREATED);
    }

    public ViewResponse<OrderState> cancelOrder(Order order, IUser user) {
        return genericCall(() -> customerController.cancelOrder(order, user));
    }

    public ViewResponse<Review> addReview(IArticle article, Review review, IUser user) {
        return genericCall(() -> customerController.addReview(article, review, user),
                ResponseStatus.CREATED);
    }

    @Override
    public CanRegisterController getCanRegisterController() {
        return customerController;
    }

    @Override
    public CanLoginController getCanLoginController() {
        return customerController;
    }
}
