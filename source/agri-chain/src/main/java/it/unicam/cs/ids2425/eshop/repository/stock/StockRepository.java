package it.unicam.cs.ids2425.eshop.repository.stock;

import it.unicam.cs.ids2425.eshop.model.stock.Stock;
import it.unicam.cs.ids2425.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> getStockByUser(User user);
}
