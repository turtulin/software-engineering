package it.unicam.cs.ids2425.article.model.article;

import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.model.ArticleType;
import it.unicam.cs.ids2425.article.model.article.compositearticle.ComposableArticle;
import it.unicam.cs.ids2425.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Package extends Article {
    @OneToMany
    private List<ComposableArticle> components;

    public Package(ArticleType type, String name, String description, double price, User seller) {
        super(type, name, description, price, seller);
        this.components = new ArrayList<>();
        if (type != ArticleType.PACKAGE) {
            throw new IllegalArgumentException("The type of a package must be PACKAGE");
        }
    }
}
