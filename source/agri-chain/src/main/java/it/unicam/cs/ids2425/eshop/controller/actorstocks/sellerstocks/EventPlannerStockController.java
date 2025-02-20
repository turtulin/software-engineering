package it.unicam.cs.ids2425.eshop.controller.actorstocks.sellerstocks;

import it.unicam.cs.ids2425.articles.model.articles.Event;
import it.unicam.cs.ids2425.eshop.controller.actorstocks.SellerStockController;
import it.unicam.cs.ids2425.eshop.model.stocks.seller.EventPlannerStock;
import it.unicam.cs.ids2425.users.model.actors.sellers.EventPlanner;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;

public class EventPlannerStockController extends SellerStockController<EventPlannerStock, EventPlanner, Event> {

    @Override
    protected SingletonRepository<EventPlannerStock> getStockRepository() {
        return SingletonRepository.getInstance(EventPlannerStock.class);
    }
}
