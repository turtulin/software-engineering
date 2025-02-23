package it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode;

import it.unicam.cs.ids2425.utilities.statuscode.BaseStatusCode;
import it.unicam.cs.ids2425.utilities.statuscode.IStatusCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OrderStatusCode implements IStatusCode {
    PENDING(BaseStatusCode.PENDING),
    PROCESSING(BaseStatusCode.PROCESSING),
    SHIPPED(BaseStatusCode.SHIPPED),
    DELIVERED(BaseStatusCode.DELIVERED),
    CANCELLED(BaseStatusCode.CANCELLED);

    private final int code;

    OrderStatusCode(BaseStatusCode baseStatus) {
        this.code = baseStatus.getCode();
    }

    public static OrderStatusCode fromCode(BaseStatusCode code) {
        return Arrays.stream(OrderStatusCode.values())
                .filter(sc -> sc.equals(code))
                .findFirst().orElse(null);
    }
}
