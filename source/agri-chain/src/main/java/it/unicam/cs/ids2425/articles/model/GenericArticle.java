package it.unicam.cs.ids2425.articles.model;

import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"name", "seller", "type"})
public abstract class GenericArticle implements IArticle {
    private String name;
    private String description;
    private String content;
    private int availableArticles;
    private double price;
    @Setter(AccessLevel.NONE)
    private ISeller seller;
    @Setter(AccessLevel.NONE)
    private ArticleType type;

    @Override
    public double getPrice() {
        return this.price;
    }
}
