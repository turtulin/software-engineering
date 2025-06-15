package it.unicam.cs.ids2425.eshop.repository.order;

import it.unicam.cs.ids2425.eshop.model.order.Order;
import it.unicam.cs.ids2425.eshop.model.order.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStateRepository extends JpaRepository<OrderState, Long> {
    List<OrderState> findAllByEntity(Order order);
}
