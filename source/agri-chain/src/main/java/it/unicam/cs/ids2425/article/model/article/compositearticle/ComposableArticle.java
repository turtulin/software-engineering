package it.unicam.cs.ids2425.article.model.article.compositearticle;

import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.model.ArticleType;
import it.unicam.cs.ids2425.user.model.User;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ComposableArticle extends Article {
    public ComposableArticle(ArticleType type, String name, String description, double price, User seller) {
        super(type, name, description, price, seller);
    }
}
