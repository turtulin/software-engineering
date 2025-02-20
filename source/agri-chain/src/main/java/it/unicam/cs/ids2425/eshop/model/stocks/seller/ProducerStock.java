package it.unicam.cs.ids2425.eshop.model.stocks.seller;

import it.unicam.cs.ids2425.articles.model.articles.RawMaterial;
import it.unicam.cs.ids2425.eshop.model.stocks.SellerStock;
import it.unicam.cs.ids2425.users.model.actors.sellers.Producer;

public class ProducerStock extends SellerStock<Producer, RawMaterial> {
    public ProducerStock(Producer owner) {
        super(owner);
    }
}
