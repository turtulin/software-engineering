package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import it.unicam.cs.ids2425.users.model.IUser;

public sealed interface IArticle permits GenericArticle {
    double getPrice();

    ArticleType getArticleType();

    IUser getSeller();

    int getAvailableArticles();

    void setAvailableArticles(int availableArticles);

    String getName();

    String getDescription();

    String getContent();
}
