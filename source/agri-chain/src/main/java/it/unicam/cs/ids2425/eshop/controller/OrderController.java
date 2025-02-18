package it.unicam.cs.ids2425.eshop.controller;

import it.unicam.cs.ids2425.eshop.model.Cart;
import it.unicam.cs.ids2425.eshop.model.order.Order;
import it.unicam.cs.ids2425.eshop.model.order.OrderState;
import it.unicam.cs.ids2425.users.controller.actors.CustomerController;
import it.unicam.cs.ids2425.users.controller.details.AddressController;
import it.unicam.cs.ids2425.users.controller.details.PaymentMethodController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.OrderStatus;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.NoSuchElementException;

@NoArgsConstructor
public class OrderController implements IController {
    private final SingletonRepository<Order> orderRepository = SingletonRepository.getInstance(Order.class);
    private final SingletonRepository<OrderState> orderStatusRepository = SingletonRepository.getInstance(OrderState.class);

    private final AddressController addressController = SingletonController.getInstance(new AddressController() {
    });
    private final PaymentMethodController paymentMethodController = SingletonController.getInstance(new PaymentMethodController() {
    });
    private final CartController cartController = SingletonController.getInstance(new CartController() {
    });
    private final CustomerController customerController = SingletonController.getInstance(new CustomerController() {
    });
    private final ShippingController shippingController = SingletonController.getInstance(new ShippingController() {
    });

    public Order create(@NonNull Cart cart, @NonNull Address shippingAddress, @NonNull Address billingAddress, @NonNull IPaymentMethod payment) {
        cart = cartController.get(cart);
        shippingAddress = addressController.get(shippingAddress);
        billingAddress = addressController.get(billingAddress);
        payment = paymentMethodController.get(payment);

        IUser user = cart.getUser();
        user = customerController.get(user, UserStatus.ACTIVE);

        Order order = new Order(cart, shippingAddress, billingAddress, payment, shippingController.getTrackingNumber(cart, shippingAddress));
        OrderState state = new OrderState(order, OrderStatus.PENDING, user);

        orderRepository.save(order);
        orderStatusRepository.save(state);

        cartController.empty(cart);

        return orderRepository.findById(order).get();
    }

    private OrderState changeOrderStatus(@NonNull Order order, OrderStatus newStatus) {
        order = orderRepository.findById(order).orElseThrow(NoSuchElementException::new);
        IUser user = order.getCart().getUser();
        user = customerController.get(user, UserStatus.ACTIVE);
        OrderState state = new OrderState(order, newStatus, user);
        orderStatusRepository.save(state);
        return orderStatusRepository.findAll().stream()
                .filter(o -> o.equals(state))
                .sorted()
                .toList().getLast();
    }

    public OrderState cancel(@NonNull Order order) {
        return changeOrderStatus(order, OrderStatus.CANCELLED);
    }

    public OrderState returnOrder(@NonNull Order order) {
        return changeOrderStatus(order, OrderStatus.EVALUATING_REFUND);
    }
}
