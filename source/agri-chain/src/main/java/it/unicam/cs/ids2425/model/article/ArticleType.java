package it.unicam.cs.ids2425.model.article;

import it.unicam.cs.ids2425.model.article.article.Event;
import it.unicam.cs.ids2425.model.article.article.Package;
import it.unicam.cs.ids2425.model.article.article.compositearticle.ProcessedProduct;
import it.unicam.cs.ids2425.model.article.article.compositearticle.RawMaterial;
import it.unicam.cs.ids2425.model.user.UserRole;
import lombok.Getter;

@Getter
public enum ArticleType {
    RAW_MATERIAL(UserRole.PRODUCER, RawMaterial.class),
    PROCESSED_PRODUCT(UserRole.TRANSFORMER, ProcessedProduct.class),
    PACKAGE(UserRole.DISTRIBUTOR, Package.class),
    EVENT(UserRole.EVENT_PLANNER, Event.class);

    private final UserRole userRole;
    private final Class<? extends Article> entityClass;

    ArticleType(UserRole userRole, Class<? extends Article> entityClass) {
        this.userRole = userRole;
        this.entityClass = entityClass;
    }

    public static ArticleType fromUserRole(UserRole userRole) {
        for (ArticleType articleType : values()) {
            if (articleType.getUserRole().equals(userRole)) {
                return articleType;
            }
        }
        throw new IllegalArgumentException("Article type not found");
    }
}
