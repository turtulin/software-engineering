package it.unicam.cs.ids2425.controller.eshop.order;

import it.unicam.cs.ids2425.controller.eshop.stock.StockController;
import it.unicam.cs.ids2425.model.eshop.order.Order;
import it.unicam.cs.ids2425.model.eshop.order.OrderState;
import it.unicam.cs.ids2425.repository.eshop.order.OrderRepository;
import it.unicam.cs.ids2425.repository.eshop.order.OrderStateRepository;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.UserRole;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.OrderStatusCode;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderStateRepository orderStateRepository;

    private final StockController stockController;

    @Autowired
    public OrderController(OrderRepository orderRepository, OrderStateRepository orderStateRepository, StockController stockController) {
        this.orderRepository = orderRepository;
        this.orderStateRepository = orderStateRepository;
        this.stockController = stockController;
    }

    @Transactional
    public Order create(@NonNull Order order, @NonNull User customer) {
        if (order.getId() != null) {
            throw new IllegalArgumentException("Should not provide Id");
        }

        order.setStock(stockController.findByUser(customer));
        stockController.order(order.getStock());

        return updateOrderStatus(order, OrderStatusCode.PENDING, null, customer);
    }

    public Order findById(@NonNull Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Transactional
    public Order cancel(@NonNull Order order, @NonNull User costumer) {
        OrderState previousState = orderStateRepository.findAllByEntity(order).getLast();
        return updateOrderStatus(order, OrderStatusCode.CANCELLED, previousState, costumer);
    }

    @Transactional
    protected Order updateOrderStatus(@NonNull Order order, @NonNull OrderStatusCode orderStatusCode, OrderState previousState, @NonNull User customer) {
        if (customer.getRole() == null || customer.getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("User must be a customer");
        }

        // no new entity created because this is the order status update, the entity data did not change.
        OrderState state = new OrderState(orderStatusCode, customer, null, order, previousState);

        orderRepository.save(order);
        orderStateRepository.save(state);

        OrderState orderState = orderStateRepository.findAllByEntity(order).getLast();
        if (!orderState.getStatusCode().equals(orderStatusCode)) {
            throw new IllegalArgumentException("Order creation failed");
        }
        return orderState.getEntity();
    }

    public List<Order> findByUser(User user) {
        return orderRepository.findAllByStock_User(user);
    }

    public OrderState getOrderStatus(Long orderId, User user) {
        Order order = findById(orderId);
        if (!order.getStock().getUser().equals(user) && user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Order does not belong to user");
        }
        return orderStateRepository.findAllByEntity(order).getLast();
    }
}
