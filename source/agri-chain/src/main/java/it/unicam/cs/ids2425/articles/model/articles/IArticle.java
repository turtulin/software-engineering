package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import it.unicam.cs.ids2425.core.identifiers.Identifiable;
import it.unicam.cs.ids2425.users.model.IUser;

public sealed interface IArticle extends Identifiable<Long> permits GenericArticle {
    double getPrice();

    ArticleType getType();

    IUser getSeller();

    String getName();

    String getDescription();
}
