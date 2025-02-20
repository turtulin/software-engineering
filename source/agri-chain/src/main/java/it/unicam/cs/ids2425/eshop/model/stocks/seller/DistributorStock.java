package it.unicam.cs.ids2425.eshop.model.stocks.seller;

import it.unicam.cs.ids2425.articles.model.articles.Package;
import it.unicam.cs.ids2425.eshop.model.stocks.SellerStock;
import it.unicam.cs.ids2425.users.model.actors.sellers.Distributor;

public class DistributorStock extends SellerStock<Distributor, Package> {
    public DistributorStock(Distributor owner) {
        super(owner);
    }
}
