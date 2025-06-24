package it.unicam.cs.ids2425.repository.eshop.stock;

import it.unicam.cs.ids2425.model.eshop.stock.StockContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockContentRepository extends JpaRepository<StockContent, Long> {
}
