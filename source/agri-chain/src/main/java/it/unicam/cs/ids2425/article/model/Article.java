package it.unicam.cs.ids2425.article.model;

import it.unicam.cs.ids2425.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Article implements IArticle {
    @Id
    @GeneratedValue
    private Long id;

    private ArticleType type;

    private String name;

    private String description;

    private double price;

    @ManyToOne
    private User seller;

    public Article(ArticleType type, String name, String description, double price, User seller) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.price = price;
        this.seller = seller;
    }
}
