package it.unicam.cs.ids2425.articles.model.states;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.articles.model.exceptions.ArticleException;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;

public class PublishedState implements IArticleState {
    @Override
    public void publish(IArticle article) {
        throw new ArticleException("Already PUBLISHED");
    }

    @Override
    public void approve(IArticle article) {
        throw new ArticleException("Already PUBLISHED");
    }

    @Override
    public void reject(IArticle article, String reason) {
        // article.setState(new RejectedState(reason));
    }

    @Override
    public ArticleStatus getStatus() {
        return ArticleStatus.PUBLISHED;
    }
}
