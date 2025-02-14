package it.unicam.cs.ids2425.core.exceptions;

import it.unicam.cs.ids2425.utilities.statuses.IState;

public class InvalidStateException extends RuntimeException {
    public InvalidStateException(IState currentState, String operation) {
        super("Invalid operation '" + operation + "' in state: " + currentState);
    }
}
