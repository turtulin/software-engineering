package it.unicam.cs.ids2425.articles.model.exceptions;

import it.unicam.cs.ids2425.core.model.ArticleId;

public class ArticleNotFoundException extends ArticleException {
    public ArticleNotFoundException(ArticleId id) {
        super("Article not found with ID: " + id.value());
    }

    public ArticleNotFoundException(String searchCriteria) {
        super("No articles matching: " + searchCriteria);
    }
}
