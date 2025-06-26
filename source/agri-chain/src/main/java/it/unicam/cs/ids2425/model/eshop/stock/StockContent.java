package it.unicam.cs.ids2425.model.eshop.stock;

import it.unicam.cs.ids2425.model.article.Article;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(of = "article")
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockContent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id")
    private Article article;

    private Long quantity;

    public StockContent(Article article, Long quantity) {
        this.article = article;
        this.quantity = quantity;
    }

    public void reduceQuantity(Long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid Quantity");
        }
        if (quantity > this.quantity) {
            throw new IllegalArgumentException("Seller Stock is not enough");
        }
        this.quantity -= quantity;
    }
}
