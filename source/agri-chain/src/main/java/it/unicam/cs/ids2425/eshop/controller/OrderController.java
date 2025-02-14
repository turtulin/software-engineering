package it.unicam.cs.ids2425.eshop.controller;

import it.unicam.cs.ids2425.eshop.model.Cart;
import it.unicam.cs.ids2425.eshop.model.Order;
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
import it.unicam.cs.ids2425.utilities.statuses.StatusInfo;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.statuses.controller.StatusInfoController;
import it.unicam.cs.ids2425.utilities.wrappers.Pair;
import it.unicam.cs.ids2425.utilities.wrappers.TypeToken;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
public class OrderController implements IController {
    private final SingletonRepository<List<Order>, Order, Order> orderRepository = SingletonRepository.getInstance(new TypeToken<>() {
    });
    private final SingletonRepository<Set<Pair<Order, List<StatusInfo<OrderStatus>>>>, Pair<Order, List<StatusInfo<OrderStatus>>>, Order> orderStatusRepository = SingletonRepository.getInstance(new TypeToken<>() {
    });

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
    private final StatusInfoController<OrderStatus> orderStatusController = SingletonController.getInstance(new StatusInfoController<OrderStatus>() {
    });

    public Order create(@NonNull Cart cart, @NonNull Address shippingAddress, @NonNull Address billingAddress, @NonNull IPaymentMethod payment) {
        cart = cartController.get(cart);
        shippingAddress = addressController.get(shippingAddress);
        billingAddress = addressController.get(billingAddress);
        payment = paymentMethodController.get(payment);

        IUser user = cart.getUser();
        user = customerController.get(user, UserStatus.ACTIVE);

        Order order = new Order(cart, shippingAddress, billingAddress, payment, shippingController.getTrackingNumber(cart, shippingAddress));

        orderRepository.create(order);

        StatusInfo<OrderStatus> status = orderStatusController.create(new StatusInfo<>(OrderStatus.PENDING, user, "Order created"), user);
        orderStatusRepository.create(new Pair<>(order, List.of(status)));

        cartController.empty(cart);

        return orderRepository.get(order);
    }

    public Pair<Order, List<StatusInfo<OrderStatus>>> cancel(@NonNull Order order) {
        order = orderRepository.get(order);
        IUser user = order.getCart().getUser();
        user = customerController.get(user, UserStatus.ACTIVE);

        StatusInfo<OrderStatus> status = orderStatusController.create(new StatusInfo<>(OrderStatus.CANCELLED, user, "Order cancelled"), user);
        Pair<Order, List<StatusInfo<OrderStatus>>> pair = orderStatusRepository.get(order);
        pair.getValue().add(status);
        orderStatusRepository.save(order, pair);

        return orderStatusRepository.get(order);
    }

    public Pair<Order, List<StatusInfo<OrderStatus>>> returnOrder(@NonNull Order order) {
        order = orderRepository.get(order);
        IUser user = order.getCart().getUser();
        user = customerController.get(user, UserStatus.ACTIVE);

        StatusInfo<OrderStatus> status = orderStatusController.create(new StatusInfo<>(OrderStatus.EVALUATING_REFUND, user, "Order refund requested"), user);
        Pair<Order, List<StatusInfo<OrderStatus>>> pair = orderStatusRepository.get(order);
        pair.getValue().add(status);
        orderStatusRepository.save(order, pair);

        return orderStatusRepository.get(order);
    }
}
