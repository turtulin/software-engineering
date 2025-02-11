package it.unicam.cs.ids2425.eshop.model;

import it.unicam.cs.ids2425.articles.model.IArticle;
import it.unicam.cs.ids2425.users.model.IUser;

public record Review(IUser user, IArticle article, float rating, String title, String comment) {
}
