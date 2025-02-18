package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import it.unicam.cs.ids2425.users.model.IUser;

public final class Event extends GenericArticle {
    public Event(String name, String desc, IUser seller, double price) {
        super(name, desc, seller, price);
    }

    @Override
    public ArticleType getType() {
        return ArticleType.EVENT;
    }
}
