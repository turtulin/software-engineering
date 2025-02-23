package it.unicam.cs.ids2425.eshop.repository.order;

import it.unicam.cs.ids2425.eshop.model.order.Order;
import it.unicam.cs.ids2425.eshop.model.order.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStateRepository extends JpaRepository<OrderState, Long> {
    List<OrderState> findAllByEntity(Order order);
}
