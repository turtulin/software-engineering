package it.unicam.cs.ids2425.eshop.model.order;

import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.statuses.OrderStatus;
import it.unicam.cs.ids2425.utilities.statuses.State;

public class OrderState extends State<Order, OrderStatus, Long> {
    public OrderState(Order entity, OrderStatus status, IUser initiator, State<Order, OrderStatus, Long> oldState) {
        super(entity, status, initiator, oldState);
    }

    public OrderState(Order entity, OrderStatus status, IUser initiator) {
        super(entity, status, initiator);
    }
}
