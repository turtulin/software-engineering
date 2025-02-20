package it.unicam.cs.ids2425.eshop.model.stocks.seller;

import it.unicam.cs.ids2425.articles.model.articles.ProcessedProduct;
import it.unicam.cs.ids2425.eshop.model.stocks.SellerStock;
import it.unicam.cs.ids2425.users.model.actors.sellers.Transformer;

public class TransformerStock extends SellerStock<Transformer, ProcessedProduct> {
    public TransformerStock(Transformer owner) {
        super(owner);
    }
}
