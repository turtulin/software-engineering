package it.unicam.cs.ids2425.eshop.controller;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import it.unicam.cs.ids2425.articles.model.articles.Package;
import it.unicam.cs.ids2425.articles.model.articles.*;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks.DistributorStockController;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks.EventPlannerStockController;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks.ProducerStockController;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks.TransformerStockController;
import it.unicam.cs.ids2425.eshop.model.order.Order;
import it.unicam.cs.ids2425.eshop.model.order.OrderState;
import it.unicam.cs.ids2425.eshop.model.stocks.Cart;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.sellers.Distributor;
import it.unicam.cs.ids2425.users.model.actors.sellers.EventPlanner;
import it.unicam.cs.ids2425.users.model.actors.sellers.Producer;
import it.unicam.cs.ids2425.users.model.actors.sellers.Transformer;
import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.OrderStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@NoArgsConstructor
public class OrderController implements IController {
    private final SingletonRepository<Order> orderRepository = SingletonRepository.getInstance(Order.class);
    private final SingletonRepository<OrderState> orderStatusRepository = SingletonRepository.getInstance(OrderState.class);

    private final ShippingController shippingController = SingletonController.getInstance(new ShippingController());

    private final ProducerStockController producerStockController = SingletonController.getInstance(new ProducerStockController());
    private final TransformerStockController transformerStockController = SingletonController.getInstance(new TransformerStockController());
    private final DistributorStockController distributorStockController = SingletonController.getInstance(new DistributorStockController());
    private final EventPlannerStockController eventPlannerStockController = SingletonController.getInstance(new EventPlannerStockController());

    public Order create(@NonNull Cart cart, @NonNull Address shippingAddress, @NonNull Address billingAddress, @NonNull IPaymentMethod payment) {
        IUser user = cart.getUser();

        Order order = new Order(cart, shippingAddress, billingAddress, payment, shippingController.getTrackingNumber(cart, shippingAddress));
        OrderState state = new OrderState(order, OrderStatus.PENDING, user);

        for (Map.Entry<IArticle, Integer> keys : cart.getArticles().entrySet()) {
            switch (keys.getKey().getType()) {
                case ArticleType.RAW_MATERIAL ->
                        producerStockController.removeArticleFromStock((RawMaterial) keys.getKey(), (Producer) keys.getKey().getSeller(), keys.getValue());
                case ArticleType.PROCESSED_PRODUCT ->
                        transformerStockController.removeArticleFromStock((ProcessedProduct) keys.getKey(), (Transformer) keys.getKey().getSeller(), keys.getValue());
                case ArticleType.PACKAGE ->
                        distributorStockController.removeArticleFromStock((Package) keys.getKey(), (Distributor) keys.getKey().getSeller(), keys.getValue());
                case ArticleType.EVENT ->
                        eventPlannerStockController.removeArticleFromStock((Event) keys.getKey(), (EventPlanner) keys.getKey().getSeller(), keys.getValue());
            }
        }

        orderRepository.save(order);
        orderStatusRepository.save(state);

        Optional<Order> o = orderRepository.findById(order);
        if (o.isPresent()) {
            return o.get();
        }
        throw new RuntimeException("Order not found");
    }

    private OrderState changeOrderStatus(@NonNull Order order, OrderStatus newStatus) {
        order = orderRepository.findById(order).orElseThrow(NoSuchElementException::new);
        IUser user = order.getCart().getUser();
        OrderState state = new OrderState(order, newStatus, user);
        orderStatusRepository.save(state);
        return orderStatusRepository.findAll().stream()
                .filter(o -> o.equals(state))
                .sorted()
                .toList().getLast();
    }

    public OrderState cancel(@NonNull Order order) {
        for (Map.Entry<IArticle, Integer> keys : order.getCart().getArticles().entrySet()) {
            switch (keys.getKey().getType()) {
                case ArticleType.RAW_MATERIAL ->
                        producerStockController.addArticleToStock((RawMaterial) keys.getKey(), (Producer) keys.getKey().getSeller(), keys.getValue());
                case ArticleType.PROCESSED_PRODUCT ->
                        transformerStockController.addArticleToStock((ProcessedProduct) keys.getKey(), (Transformer) keys.getKey().getSeller(), keys.getValue());
                case ArticleType.PACKAGE ->
                        distributorStockController.addArticleToStock((Package) keys.getKey(), (Distributor) keys.getKey().getSeller(), keys.getValue());
                case ArticleType.EVENT ->
                        eventPlannerStockController.addArticleToStock((Event) keys.getKey(), (EventPlanner) keys.getKey().getSeller(), keys.getValue());
            }
        }
        return changeOrderStatus(order, OrderStatus.CANCELLED);
    }
}
