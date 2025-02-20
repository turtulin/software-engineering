package it.unicam.cs.ids2425.utilities.wrappers.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ViewResponse<T> {
    private final ResponseStatus status;
    private final T data;
    private final String message;
}
