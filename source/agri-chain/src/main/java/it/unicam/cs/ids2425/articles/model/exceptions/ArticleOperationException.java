package it.unicam.cs.ids2425.articles.model.exceptions;

import it.unicam.cs.ids2425.core.model.ArticleId;

public class ArticleOperationException extends ArticleException {
    public ArticleOperationException(ArticleId id, String operation) {
        super(String.format("Cannot %s article %s", operation, id.value()));
    }
}
