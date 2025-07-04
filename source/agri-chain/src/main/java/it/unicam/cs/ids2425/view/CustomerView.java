package it.unicam.cs.ids2425.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.cs.ids2425.controller.article.actor.CustomerArticleController;
import it.unicam.cs.ids2425.model.eshop.order.Order;
import it.unicam.cs.ids2425.model.eshop.order.OrderState;
import it.unicam.cs.ids2425.model.eshop.review.Review;
import it.unicam.cs.ids2425.model.eshop.stock.Stock;
import it.unicam.cs.ids2425.model.eshop.stock.StockContent;
import it.unicam.cs.ids2425.controller.problem.ProblemController;
import it.unicam.cs.ids2425.controller.user.OtherUserController;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.detail.address.Address;
import it.unicam.cs.ids2425.model.user.detail.payment.AbstractPaymentMethod;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Getter
@RestController
@RequestMapping("/customer")
public class CustomerView implements IView, ICanLogoutView, ICanRegisterView, ICanReportView {
    private final OtherUserController logoutController;
    private final OtherUserController registerController;
    private final CustomerArticleController customerArticleController;
    private final ProblemController problemController;

    public CustomerView(OtherUserController logoutController, CustomerArticleController customerArticleController, ProblemController problemController) {
        this.logoutController = logoutController;
        this.registerController = logoutController;
        this.customerArticleController = customerArticleController;
        this.problemController = problemController;
    }

    @RequestMapping(value = "/cart", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<Stock>> getCart(@RequestAttribute("user") User user) {
        return genericCall(() -> customerArticleController.getCart(user));
    }

    @RequestMapping(value = "/cart/{id}/{quantity}", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<StockContent>> addToCart(@PathVariable("id") Long articleId,
                                                                @PathVariable("quantity") Long quantity,
                                                                @RequestAttribute("user") User user) {
        return genericCall(() -> customerArticleController.addToCart(articleId, quantity, user));
    }

    @RequestMapping(value = "/cart/{id}/{quantity}", method = {RequestMethod.DELETE})
    public ResponseEntity<ViewResponse<StockContent>> removeFromCart(@PathVariable("id") Long articleId,
                                                                     @PathVariable("quantity") Long quantity,
                                                                     @RequestAttribute("user") User user) {
        return genericCall(() -> customerArticleController.removeFromCart(articleId, quantity, user));
    }

    @RequestMapping(value = "/cart/checkout", method = {RequestMethod.POST})
    public ResponseEntity<ViewResponse<Order>> checkout(@RequestBody JsonNode body,
                                                        @RequestAttribute("user") User user) {
        ObjectMapper mapper = new ObjectMapper();
        Order order = new Order();
        Address shippingAddress = mapper.convertValue(body.get("shippingAddress"), Address.class);
        order.setShippingAddress(shippingAddress);
        Address billingAddress = mapper.convertValue(body.get("billingAddress"), Address.class);
        order.setBillingAddress(billingAddress);
        AbstractPaymentMethod payment = mapper.convertValue(body.get("payment"), AbstractPaymentMethod.class);
        order.setPayment(payment);
        return genericCall(() -> customerArticleController.checkout(order, user));
    }

    @RequestMapping(value = "/order/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity<ViewResponse<Order>> cancelOrder(@PathVariable("id") Long orderId,
                                                           @RequestAttribute("user") User user) {
        return genericCall(() -> customerArticleController.cancelOrder(orderId, user));
    }

    @RequestMapping(value = "/order/{id}/status", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<OrderState>> getOrderStatus(@PathVariable("id") Long orderId,
                                                                   @RequestAttribute("user") User user) {
        return genericCall(() -> customerArticleController.getOrderStatus(orderId, user));
    }

    @RequestMapping(value = "/order/all", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<Order>>> getOrders(@RequestAttribute("user") User user) {
        return genericCall(() -> customerArticleController.getOrders(user));
    }

    @RequestMapping(value = "/review", method = {RequestMethod.POST})
    public ResponseEntity<ViewResponse<Review>> review(@RequestBody Review review,
                                                       @RequestAttribute("user") User user) {
        return genericCall(() -> customerArticleController.review(review, user));
    }
}
