package it.unicam.cs.ids2425.utilities.statuses;

import it.unicam.cs.ids2425.utilities.statuses.statusconfigs.StatusCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BaseStatus implements IStatus{
    DELETED(StatusCodes.DELETED),
    ACTIVE(StatusCodes.ACTIVE),
    INACTIVE(StatusCodes.INACTIVE),
    PENDING(StatusCodes.PENDING);

    private final StatusCodes statusCodes;
}
