package it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode;

import it.unicam.cs.ids2425.utilities.statuscode.BaseStatusCode;
import it.unicam.cs.ids2425.utilities.statuscode.IStatusCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ProblemStatusCode implements IStatusCode {
    OPEN(BaseStatusCode.OPEN),
    CLOSED(BaseStatusCode.CLOSED),
    SOLVED(BaseStatusCode.SOLVED),
    REJECTED(BaseStatusCode.REJECTED),
    UNRESOLVED(BaseStatusCode.UNRESOLVED);

    private final int code;

    ProblemStatusCode(BaseStatusCode baseStatus) {
        this.code = baseStatus.getCode();
    }

    public static ProblemStatusCode fromCode(BaseStatusCode code) {
        return Arrays.stream(ProblemStatusCode.values())
                .filter(sc -> sc.equals(code))
                .findFirst().orElse(null);
    }
}
