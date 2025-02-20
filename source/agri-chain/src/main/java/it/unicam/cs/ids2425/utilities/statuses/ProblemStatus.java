package it.unicam.cs.ids2425.utilities.statuses;

import it.unicam.cs.ids2425.utilities.statuses.statusconfigs.StatusCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProblemStatus implements IStatus {
    OPEN(StatusCodes.ACTIVE),
    CLOSED(StatusCodes.INACTIVE),
    RESOLVED(StatusCodes.COMPLETED),
    UNRESOLVED(StatusCodes.REJECTED);

    private final StatusCodes statusCodes;
}
