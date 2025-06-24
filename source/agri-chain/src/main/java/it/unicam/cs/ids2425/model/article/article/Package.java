package it.unicam.cs.ids2425.model.article.article;

import it.unicam.cs.ids2425.model.article.Article;
import it.unicam.cs.ids2425.model.article.ArticleType;
import it.unicam.cs.ids2425.model.article.HasComponent;
import it.unicam.cs.ids2425.model.article.article.compositearticle.ComposableArticle;
import it.unicam.cs.ids2425.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Package extends Article implements HasComponent {
    @ManyToMany(fetch = FetchType.EAGER)
    private List<ComposableArticle> components;

    public Package(ArticleType type, String name, String description, double price, User seller) {
        super(type, name, description, price, seller);
        this.components = new ArrayList<>();
        if (type != ArticleType.PACKAGE) {
            throw new IllegalArgumentException("The type of a package must be PACKAGE");
        }
    }

    @Override
    public Package clone() {
        Package clone = new Package(getType(), getName(), getDescription(), getPrice(), getSeller());
        if (this.components == null) {
            clone.components = new ArrayList<>();
        } else {
            clone.components = new ArrayList<>(components);
        }
        return clone;
    }
}
