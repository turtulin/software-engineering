package it.unicam.cs.ids2425.utilities.statuses;

import it.unicam.cs.ids2425.utilities.statuses.statusconfigs.StatusCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus implements IStatus {
    PENDING(StatusCodes.PENDING),
    ACTIVE(StatusCodes.ACTIVE),
    BANNED(StatusCodes.INACTIVE),
    DELETED(StatusCodes.DELETED);

    private final StatusCodes statusCodes;
}
