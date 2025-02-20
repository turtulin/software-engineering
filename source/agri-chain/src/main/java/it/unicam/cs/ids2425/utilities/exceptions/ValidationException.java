package it.unicam.cs.ids2425.utilities.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String field, String reason) {
        super("Validation failed for " + field + ": " + reason);
    }
}
