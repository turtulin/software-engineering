package it.unicam.cs.ids2425.utilities.wrappers.responses;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    CONFLICT(409),
    INTERNAL_SERVER_ERROR(500);

    private final int status;

    ResponseStatus(int status) {
        this.status = status;
    }
}
