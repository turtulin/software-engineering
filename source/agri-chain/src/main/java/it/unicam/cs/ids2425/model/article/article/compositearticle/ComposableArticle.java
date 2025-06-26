package it.unicam.cs.ids2425.model.article.article.compositearticle;

import it.unicam.cs.ids2425.model.article.Article;
import it.unicam.cs.ids2425.model.article.ArticleType;
import it.unicam.cs.ids2425.model.user.User;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class ComposableArticle extends Article {
    public ComposableArticle(ArticleType type, String name, String description, double price, User seller) {
        super(type, name, description, price, seller);
    }

    @Override
    public ComposableArticle clone() {
        throw new UnsupportedOperationException("Cloning of ComposableArticle is not supported, try cloning one of its subclasses instead.");
    }
}
