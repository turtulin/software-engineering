package it.unicam.cs.ids2425.article.model;

import it.unicam.cs.ids2425.user.model.UserRole;
import lombok.Getter;

@Getter
public enum ArticleType {
    RAW_MATERIAL(UserRole.PRODUCER),
    PROCESSED_PRODUCT(UserRole.TRANSFORMER),
    PACKAGE(UserRole.DISTRIBUTOR),
    EVENT(UserRole.EVENT_PLANNER);

    private final UserRole userRole;

    ArticleType(UserRole userRole) {
        this.userRole = userRole;
    }
}
