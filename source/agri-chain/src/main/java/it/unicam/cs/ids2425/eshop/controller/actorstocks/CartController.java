package it.unicam.cs.ids2425.eshop.controller.actorstocks;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.controller.StockController;
import it.unicam.cs.ids2425.eshop.model.stocks.Cart;
import it.unicam.cs.ids2425.users.model.actors.Customer;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CartController extends StockController<Cart, Customer, IArticle> {

    @Override
    protected SingletonRepository<Cart> getStockRepository() {
        return SingletonRepository.getInstance(Cart.class);
    }
}
