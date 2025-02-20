package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.eshop.model.stocks.seller.ProducerStock;
import it.unicam.cs.ids2425.users.model.UserRole;

public final class Producer extends GenericSeller {
    public Producer(String username, String password) {
        super(username, password);
        this.stock = new ProducerStock(this);
    }

    @Override
    public UserRole getRole() {
        return UserRole.PRODUCER;
    }
}
