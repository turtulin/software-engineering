package it.unicam.cs.ids2425.eshop.repository.order;

import it.unicam.cs.ids2425.eshop.model.order.Order;
import it.unicam.cs.ids2425.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStock_User(User user);
}
