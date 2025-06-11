package it.unicam.cs.ids2425.article.model.article.compositearticle;

import it.unicam.cs.ids2425.article.model.ArticleType;
import it.unicam.cs.ids2425.article.model.HasComponent;
import it.unicam.cs.ids2425.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true, of ={})
@Data
@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProcessedProduct extends ComposableArticle implements HasComponent {
    @ManyToMany(fetch = FetchType.EAGER)
    private List<ComposableArticle> components;

    public ProcessedProduct(ArticleType type, String name, String description, double price, User seller) {
        super(type, name, description, price, seller);
        this.components = new ArrayList<>();
        if (type != ArticleType.PROCESSED_PRODUCT) {
            throw new IllegalArgumentException("The type of a raw material must be PROCESSED_PRODUCT");
        }
    }

    @Override
    public ProcessedProduct clone() {
        ProcessedProduct clone = new ProcessedProduct(this.getType(), this.getName(), this.getDescription(), this.getPrice(), this.getSeller());
        if (this.components == null) {
            clone.components = new ArrayList<>();
        } else {
            clone.components = new ArrayList<>(this.components);
        }
        return clone;
    }
}
