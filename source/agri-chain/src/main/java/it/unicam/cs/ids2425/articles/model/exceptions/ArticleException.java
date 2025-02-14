package it.unicam.cs.ids2425.articles.model.exceptions;

public class ArticleException extends RuntimeException {
    public ArticleException(String message) {
        super(message);
    }

    public ArticleException(String message, Throwable cause) {
        super(message, cause);
    }
}
