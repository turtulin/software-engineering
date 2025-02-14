package it.unicam.cs.ids2425.articles.model.exceptions;

import it.unicam.cs.ids2425.utilities.statuses.IState;

public class InvalidArticleStateException extends ArticleException {
    public InvalidArticleStateException(IState currentState, String operation) {
        super(String.format("Cannot %s while in %s state", operation, currentState));
    }

    public InvalidArticleStateException(String message) {
        super(message);
    }
}
