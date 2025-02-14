package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import it.unicam.cs.ids2425.core.model.ArticleId;
import it.unicam.cs.ids2425.core.model.UserId;

public final class Event extends GenericArticle {
    public Event(ArticleId id, String name, String desc, UserId sellerId, double price) {
        super(id, name, desc, sellerId, price);
    }

    @Override
    public ArticleType getType() {
        return ArticleType.EVENT;
    }
}
