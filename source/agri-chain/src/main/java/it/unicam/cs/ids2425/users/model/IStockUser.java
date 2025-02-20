package it.unicam.cs.ids2425.users.model;

import it.unicam.cs.ids2425.eshop.model.stocks.GenericStock;

public interface IStockUser extends IUser {
    GenericStock<?, ?> getStock();

    void setStock(GenericStock<?, ?> stock);
}
