package it.unicam.cs.ids2425.article.model;

import it.unicam.cs.ids2425.user.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class Article implements IArticle, Cloneable {
    @Id
    @GeneratedValue
    private Long id;

    private ArticleType type;

    private String name;

    private String description;

    private Double price;

    @ManyToOne
    private User seller;

    public Article(ArticleType type, String name, String description, double price, User seller) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.price = price;
        this.seller = seller;
    }

    @Override
    public abstract Article clone();
}
