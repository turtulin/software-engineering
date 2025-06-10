package it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode;

import it.unicam.cs.ids2425.utilities.statuscode.BaseStatusCode;
import it.unicam.cs.ids2425.utilities.statuscode.IStatusCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ArticleStatusCode implements IStatusCode {
    DELETED(BaseStatusCode.DELETED),
    PUBLISHED(BaseStatusCode.PUBLISHED),
    REJECTED(BaseStatusCode.REJECTED),
    DRAFT(BaseStatusCode.DRAFT),
    ARCHIVED(BaseStatusCode.CLOSED),
    PENDING(BaseStatusCode.PENDING);

    private final int code;

    ArticleStatusCode(BaseStatusCode baseStatus) {
        this.code = baseStatus.getCode();
    }

    public static ArticleStatusCode fromCode(BaseStatusCode code) {
        return Arrays.stream(ArticleStatusCode.values())
                .filter(sc -> sc.equals(code))
                .findFirst().orElse(null);
    }
}
