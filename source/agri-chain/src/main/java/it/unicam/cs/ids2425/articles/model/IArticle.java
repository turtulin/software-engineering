package it.unicam.cs.ids2425.articles.model;

import it.unicam.cs.ids2425.users.model.IUser;

public interface IArticle {
    double getPrice();

    ArticleType getArticleType();

    IUser getSeller();

    int getAvailableArticles();

    void setAvailableArticles(int availableArticles);

    String getName();

    String getDescription();

    String getContent();
}
