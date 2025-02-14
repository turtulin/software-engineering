package it.unicam.cs.ids2425.articles.model.states;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.articles.model.exceptions.ArticleException;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.statuses.IState;

public interface IArticleState extends IState {
    void publish(IArticle article) throws ArticleException;

    void approve(IArticle article) throws ArticleException;

    void reject(IArticle article, String reason) throws ArticleException;

    ArticleStatus getStatus();
}
