package it.unicam.cs.ids2425.repository.eshop.stock;

import it.unicam.cs.ids2425.model.eshop.stock.Stock;
import it.unicam.cs.ids2425.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> getStockByUser(User user);
}
