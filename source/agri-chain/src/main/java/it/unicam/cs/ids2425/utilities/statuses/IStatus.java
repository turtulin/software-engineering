package it.unicam.cs.ids2425.utilities.statuses;

import it.unicam.cs.ids2425.utilities.statuses.statusconfigs.StatusCodes;

public sealed interface IStatus permits BaseStatus, ArticleStatus, OrderStatus {
    StatusCodes getStatusCodes();

    default int getCode() {
        return getStatusCodes().code();
    }

    default String getName() {
        return getStatusCodes().displayName();
    }
}
