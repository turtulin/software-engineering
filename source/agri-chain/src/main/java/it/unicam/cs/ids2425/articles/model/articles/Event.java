package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class Event extends GenericArticle {
    @Override
    public ArticleType getArticleType() {
        return ArticleType.EVENT;
    }
}
