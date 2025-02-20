package it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks;

import it.unicam.cs.ids2425.articles.model.articles.Package;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.SellerStockController;
import it.unicam.cs.ids2425.eshop.model.stocks.seller.DistributorStock;
import it.unicam.cs.ids2425.users.model.actors.sellers.Distributor;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;

public class DistributorStockController extends SellerStockController<DistributorStock, Distributor, Package> {

    @Override
    protected SingletonRepository<DistributorStock> getStockRepository() {
        return SingletonRepository.getInstance(DistributorStock.class);
    }
}
