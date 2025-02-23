package it.unicam.cs.ids2425.eshop.model.order;

import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.utilities.state.AbstractState;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.OrderStatusCode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderState extends AbstractState {
    @ManyToOne
    private Order entity;

    @OneToOne(fetch = FetchType.LAZY)
    private OrderState oldState;

    public OrderState(OrderStatusCode articleStatusCode, User initiator, String reason, Order entity, OrderState oldState) {
        super(articleStatusCode, initiator, reason);
        this.entity = entity;
        this.oldState = oldState;
    }
}
