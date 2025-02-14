package it.unicam.cs.ids2425.articles.model.states;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.articles.model.exceptions.ArticleException;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;

public class DraftState implements IArticleState {
    @Override
    public void publish(IArticle article) {
        // article.setStatus(ArticleStatus.PUBLISHED);
    }

    @Override
    public void approve(IArticle article) {
        throw new ArticleException("Cannot approve from DRAFT state");
    }

    @Override
    public void reject(IArticle article, String reason) {
        throw new ArticleException("Cannot reject from DRAFT state");
    }

    @Override
    public ArticleStatus getStatus() {
        return ArticleStatus.DRAFT;
    }
}
