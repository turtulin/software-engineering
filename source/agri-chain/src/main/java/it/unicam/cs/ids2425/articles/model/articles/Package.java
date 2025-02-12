package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class Package extends CompositeArticle {

    @Override
    public boolean canContain(IArticle article) {
        return (article.getArticleType() == ArticleType.RAW_MATERIAL ||
                article.getArticleType() == ArticleType.PROCESSED_PRODUCT ||
                article.getArticleType() == ArticleType.PACKAGE);
    }

    @Override
    public ArticleType getArticleType() {
        return ArticleType.PACKAGE;
    }
}
