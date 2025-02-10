package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import it.unicam.cs.ids2425.articles.model.IArticle;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProcessedProduct extends CompositeArticle {
    @Override
    public boolean canContain(IArticle article) {
        return (article.getArticleType() == ArticleType.RAW_MATERIAL ||
                article.getArticleType() == ArticleType.PROCESSED_PRODUCT);
    }

    @Override
    public ArticleType getArticleType() {
        return ArticleType.PROCESSED_PRODUCT;
    }
}
