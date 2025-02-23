package it.unicam.cs.ids2425.utilities.statuscode;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum BaseStatusCode implements IStatusCode {
    DELETED(0),
    ACTIVE(1),
    INACTIVE(2),
    PUBLISHED(3),
    REJECTED(4),
    DRAFT(5),
    PENDING(6),
    PROCESSING(7),
    SHIPPED(8),
    DELIVERED(9),
    CANCELLED(10),
    REFUNDED(11),
    COMPLETED(12),
    EVALUATING_REFUND(13),
    OPEN(14),
    CLOSED(15),
    SOLVED(16),
    UNRESOLVED(17),
    BANNED(18);

    private final int code;

    public static BaseStatusCode fromCode(IStatusCode code) {
        return Arrays.stream(BaseStatusCode.values())
                .filter(sc -> sc.equals(code))
                .findFirst().orElse(null);
    }
}
