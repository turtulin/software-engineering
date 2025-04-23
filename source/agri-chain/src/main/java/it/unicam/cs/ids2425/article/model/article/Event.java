package it.unicam.cs.ids2425.article.model.article;

import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.model.ArticleType;
import it.unicam.cs.ids2425.user.model.User;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Event extends Article {
    public Event(ArticleType type, String name, String description, double price, User seller) {
        super(type, name, description, price, seller);
        if (type != ArticleType.EVENT) {
            throw new IllegalArgumentException("The type of a package must be EVENT");
        }
    }
}
