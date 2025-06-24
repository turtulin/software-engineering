package it.unicam.cs.ids2425.model.article.article.compositearticle;

import it.unicam.cs.ids2425.model.article.ArticleType;
import it.unicam.cs.ids2425.model.user.User;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RawMaterial extends ComposableArticle {
    public RawMaterial(ArticleType type, String name, String description, double price, User seller) {
        super(type, name, description, price, seller);
        if (type != ArticleType.RAW_MATERIAL) {
            throw new IllegalArgumentException("The type of a raw material must be RAW_MATERIAL");
        }
    }

    @Override
    public RawMaterial clone() {
        return new RawMaterial(this.getType(), this.getName(), this.getDescription(), this.getPrice(), this.getSeller());
    }
}
