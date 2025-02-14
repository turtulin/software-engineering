package it.unicam.cs.ids2425.utilities.statuses;

import it.unicam.cs.ids2425.utilities.statuses.statusconfigs.StatusCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ArticleStatus implements IStatus {
    DELETED(StatusCodes.DELETED),
    PUBLISHED(StatusCodes.PUBLISHED),
    REJECTED(StatusCodes.REJECTED),
    DRAFT(StatusCodes.DRAFT),
    PENDING(StatusCodes.PENDING);

    private final StatusCodes statusCodes;

}
