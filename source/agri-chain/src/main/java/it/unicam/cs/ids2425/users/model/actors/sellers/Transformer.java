package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.eshop.model.stocks.seller.TransformerStock;
import it.unicam.cs.ids2425.users.model.UserRole;

public final class Transformer extends GenericSeller {
    public Transformer(String username, String password) {
        super(username, password);
        this.stock = new TransformerStock(this);
    }

    @Override
    public UserRole getRole() {
        return UserRole.TRANSFORMER;
    }
}
