package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import it.unicam.cs.ids2425.core.model.ArticleId;
import it.unicam.cs.ids2425.core.model.UserId;

public final class ProcessedProduct extends CompositeArticle {
    public ProcessedProduct(ArticleId id, String name, String description, UserId sellerId, double price) {
        super(id, name, description, sellerId, price);
    }

    @Override
    public boolean canContain(IArticle article) {
        return (article.getType() == ArticleType.RAW_MATERIAL ||
                article.getType() == ArticleType.PROCESSED_PRODUCT);
    }

    @Override
    public ArticleType getType() {
        return ArticleType.PROCESSED_PRODUCT;
    }

}
