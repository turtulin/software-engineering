package it.unicam.cs.ids2425.articles.model.exceptions;

public class ArticleValidationException extends ArticleException {
    public ArticleValidationException(String field, String reason) {
        super(String.format("Invalid %s: %s", field, reason));
    }

    public ArticleValidationException(String message) {
        super(message);
    }
}
