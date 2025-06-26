package it.unicam.cs.ids2425.model.article.article;

import it.unicam.cs.ids2425.model.article.Article;
import it.unicam.cs.ids2425.model.article.ArticleType;
import it.unicam.cs.ids2425.model.user.User;
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
    @Override
    public Event clone() {
        return new Event(this.getType(), this.getName(), this.getDescription(), this.getPrice(), this.getSeller());
    }
}
