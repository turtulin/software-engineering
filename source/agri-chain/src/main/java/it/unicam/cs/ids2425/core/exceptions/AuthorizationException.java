package it.unicam.cs.ids2425.core.exceptions;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String role, String operation) {
        super("Role '" + role + "' cannot perform operation: " + operation);
    }
}
