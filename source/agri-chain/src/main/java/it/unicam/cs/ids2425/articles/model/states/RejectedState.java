package it.unicam.cs.ids2425.articles.model.states;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.articles.model.exceptions.ArticleException;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;

public class RejectedState implements IArticleState {
    private final String rejectionReason;

    public RejectedState(String reason) {
        this.rejectionReason = reason;
    }

    @Override
    public void publish(IArticle article) {
        // article.setState(new PendingState());
    }

    @Override
    public void approve(IArticle article) {
        throw new ArticleException("Cannot approve REJECTED article, already REJECTED with reason: " + rejectionReason);
    }

    @Override
    public void reject(IArticle article, String reason) {
        throw new ArticleException("Already REJECTED");
    }

    @Override
    public ArticleStatus getStatus() {
        return ArticleStatus.REJECTED;
    }
}
