package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.users.model.IUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public sealed abstract class GenericArticle implements IArticle permits RawMaterial, Event, CompositeArticle {
    private static long lastId = 0L;
    private final Long id;
    private String name;
    private String description;
    private IUser seller;
    private double price;

    public GenericArticle(String name, String description, IUser seller, double price) {
        this.id = ++lastId;
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.price = price;
    }
}
