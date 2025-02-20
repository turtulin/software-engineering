package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.eshop.model.stocks.seller.DistributorStock;
import it.unicam.cs.ids2425.users.model.UserRole;

public final class Distributor extends GenericSeller {
    public Distributor(String username, String password) {
        super(username, password);
        this.stock = new DistributorStock(this);
    }

    @Override
    public UserRole getRole() {
        return UserRole.DISTRIBUTOR;
    }
}
