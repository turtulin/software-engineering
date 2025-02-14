package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import it.unicam.cs.ids2425.core.model.UserId;

public sealed interface IArticle permits GenericArticle {
    double getPrice();

    ArticleType getType();

    UserId getSellerId();

    String getName();

    String getDescription();
}
