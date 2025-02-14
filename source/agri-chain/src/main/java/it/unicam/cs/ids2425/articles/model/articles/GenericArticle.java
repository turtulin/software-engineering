package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.core.model.ArticleId;
import it.unicam.cs.ids2425.core.model.AuditableEntity;
import it.unicam.cs.ids2425.core.model.UserId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public sealed abstract class GenericArticle extends AuditableEntity implements IArticle permits RawMaterial, Event, CompositeArticle {
    protected final ArticleId id;
    protected String name;
    protected String description;
    protected UserId sellerId;
    protected double price;

    public GenericArticle(ArticleId id, String name, String description, UserId sellerId, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sellerId = sellerId;
        this.price = price;
    }

}
