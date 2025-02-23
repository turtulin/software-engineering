package it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode;

import it.unicam.cs.ids2425.utilities.statuscode.BaseStatusCode;
import it.unicam.cs.ids2425.utilities.statuscode.IStatusCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserStatusCode implements IStatusCode {
    PENDING(BaseStatusCode.PENDING),
    ACTIVE(BaseStatusCode.ACTIVE),
    INACTIVE(BaseStatusCode.INACTIVE),
    BANNED(BaseStatusCode.BANNED),
    DELETED(BaseStatusCode.DELETED);

    private final int code;

    UserStatusCode(BaseStatusCode baseStatus) {
        this.code = baseStatus.getCode();
    }

    public static UserStatusCode fromCode(BaseStatusCode code) {
        return Arrays.stream(UserStatusCode.values())
                .filter(sc -> sc.equals(code))
                .findFirst().orElse(null);
    }
}
