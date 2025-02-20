package it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks;

import it.unicam.cs.ids2425.articles.model.articles.RawMaterial;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.SellerStockController;
import it.unicam.cs.ids2425.eshop.model.stocks.seller.ProducerStock;
import it.unicam.cs.ids2425.users.model.actors.sellers.Producer;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;

public class ProducerStockController extends SellerStockController<ProducerStock, Producer, RawMaterial> {

    @Override
    protected SingletonRepository<ProducerStock> getStockRepository() {
        return SingletonRepository.getInstance(ProducerStock.class);
    }
}
