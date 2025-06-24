package it.unicam.cs.ids2425.repository.eshop.order;

import it.unicam.cs.ids2425.model.eshop.order.Order;
import it.unicam.cs.ids2425.model.eshop.order.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStateRepository extends JpaRepository<OrderState, Long> {
    List<OrderState> findAllByEntity(Order order);
}
