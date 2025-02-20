package it.unicam.cs.ids2425.eshop.model.stocks.seller;

import it.unicam.cs.ids2425.articles.model.articles.Event;
import it.unicam.cs.ids2425.eshop.model.stocks.SellerStock;
import it.unicam.cs.ids2425.users.model.actors.sellers.EventPlanner;

public class EventPlannerStock extends SellerStock<EventPlanner, Event> {
    public EventPlannerStock(EventPlanner owner) {
        super(owner);
    }
}
