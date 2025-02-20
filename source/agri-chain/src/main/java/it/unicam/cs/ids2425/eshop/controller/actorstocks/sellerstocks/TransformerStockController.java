package it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks;

import it.unicam.cs.ids2425.articles.model.articles.ProcessedProduct;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.SellerStockController;
import it.unicam.cs.ids2425.eshop.model.stocks.seller.TransformerStock;
import it.unicam.cs.ids2425.users.model.actors.sellers.Transformer;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;

public class TransformerStockController extends SellerStockController<TransformerStock, Transformer, ProcessedProduct> {

    @Override
    protected SingletonRepository<TransformerStock> getStockRepository() {
        return SingletonRepository.getInstance(TransformerStock.class);
    }
}
