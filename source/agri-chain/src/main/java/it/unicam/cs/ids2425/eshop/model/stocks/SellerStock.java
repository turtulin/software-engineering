package it.unicam.cs.ids2425.eshop.model.stocks;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;

public class SellerStock<T extends ISeller, S extends IArticle> extends GenericStock<T, S> {
    public SellerStock(T owner) {
        super(owner);
    }
}
