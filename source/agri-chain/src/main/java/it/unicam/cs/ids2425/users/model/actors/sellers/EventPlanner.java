package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.eshop.model.stocks.seller.EventPlannerStock;
import it.unicam.cs.ids2425.users.model.UserRole;

public final class EventPlanner extends GenericSeller {
    public EventPlanner(String username, String password) {
        super(username, password);
        this.stock = new EventPlannerStock(this);
    }

    @Override
    public UserRole getRole() {
        return UserRole.EVENT_PLANNER;
    }
}
