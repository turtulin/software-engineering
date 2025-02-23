package it.unicam.cs.ids2425.eshop.model.stock;

import it.unicam.cs.ids2425.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<StockContent> articles;

    public Stock(User user) {
        this.user = user;
        articles = new ArrayList<>();
    }

    public void empty() {
        articles.clear();
    }

    public Stock clone() {
        try {
            Stock stock = (Stock) super.clone();
            // Stock stock = this.getClass().getConstructor(user.getClass()).newInstance(user);
            stock.articles.clear();
            stock.articles.addAll(this.articles);
            stock.user = this.user;
            return stock;
        } /*catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }*/ catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
