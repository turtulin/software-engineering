package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.articles.model.ArticleType;
import it.unicam.cs.ids2425.users.model.IUser;

public final class ProcessedProduct extends CompositeArticle {
    public ProcessedProduct(String name, String description, IUser seller, double price) {
        super(name, description, seller, price);
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
