package it.unicam.cs.ids2425.article.model;

import it.unicam.cs.ids2425.article.model.article.Event;
import it.unicam.cs.ids2425.article.model.article.Package;
import it.unicam.cs.ids2425.article.model.article.compositearticle.ProcessedProduct;
import it.unicam.cs.ids2425.article.model.article.compositearticle.RawMaterial;
import it.unicam.cs.ids2425.user.model.UserRole;
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
}
