package it.unicam.cs.ids2425.articles.model;

import it.unicam.cs.ids2425.utilities.statuses.BaseStatus;

public interface IArticle {
    ArticleType getArticleType();
    void setStatus(BaseStatus status);
}
