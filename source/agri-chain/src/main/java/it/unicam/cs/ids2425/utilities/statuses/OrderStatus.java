package it.unicam.cs.ids2425.utilities.statuses;

import it.unicam.cs.ids2425.utilities.statuses.statusconfigs.StatusCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus implements IStatus {
    PENDING(StatusCodes.PENDING),
    PROCESSING(StatusCodes.PROCESSING),
    SHIPPED(StatusCodes.SHIPPED),
    DELIVERED(StatusCodes.DELIVERED),
    CANCELLED(StatusCodes.CANCELLED);

    private final StatusCodes statusCodes;
}
