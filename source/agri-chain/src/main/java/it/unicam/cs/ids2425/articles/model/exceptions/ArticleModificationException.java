package it.unicam.cs.ids2425.articles.model.exceptions;

import it.unicam.cs.ids2425.core.model.ArticleId;

public class ArticleModificationException extends ArticleException {
    public ArticleModificationException(ArticleId id) {
        super("Cannot modify article " + id.value() + " in its current state");
    }
}
