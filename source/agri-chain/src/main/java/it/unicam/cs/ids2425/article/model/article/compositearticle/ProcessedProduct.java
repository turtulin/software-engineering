package it.unicam.cs.ids2425.article.model.article.compositearticle;

import it.unicam.cs.ids2425.article.model.ArticleType;
import it.unicam.cs.ids2425.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProcessedProduct extends ComposableArticle {
    @ManyToMany(fetch = FetchType.EAGER)
    private List<ComposableArticle> ingredients;

    public ProcessedProduct(ArticleType type, String name, String description, double price, User seller) {
        super(type, name, description, price, seller);
        this.ingredients = new ArrayList<>();
        if (type != ArticleType.PROCESSED_PRODUCT) {
            throw new IllegalArgumentException("The type of a raw material must be PROCESSED_PRODUCT");
        }
    }
}
