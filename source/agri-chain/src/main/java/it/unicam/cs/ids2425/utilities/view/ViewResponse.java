package it.unicam.cs.ids2425.utilities.view;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class ViewResponse<T> {
    private final T data;
    private final String message;
    private final HttpStatus status;
}
