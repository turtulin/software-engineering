package it.unicam.cs.ids2425.articles.model.exceptions;

import it.unicam.cs.ids2425.core.model.ArticleId;
import it.unicam.cs.ids2425.core.model.UserId;

public class ArticleAuthorizationException extends ArticleException {
    public ArticleAuthorizationException(UserId userId, ArticleId articleId) {
        super(String.format("User %s not authorized to modify article %s",
                userId.value(), articleId.value()));
    }

    public ArticleAuthorizationException(String message) {
        super(message);
    }
}
