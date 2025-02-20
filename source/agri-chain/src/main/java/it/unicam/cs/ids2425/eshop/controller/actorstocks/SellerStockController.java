package it.unicam.cs.ids2425.eshop.controller.actorstocks;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.controller.StockController;
import it.unicam.cs.ids2425.eshop.model.stocks.SellerStock;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class SellerStockController<T extends SellerStock<S, Q>, S extends ISeller, Q extends IArticle>
        extends StockController<T, S, Q> {
}
