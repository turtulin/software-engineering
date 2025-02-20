package it.unicam.cs.ids2425.eshop.model.stocks;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.users.model.actors.Customer;

public class Cart extends GenericStock<Customer, IArticle> {
    public Cart(Customer user) {
        super(user);
    }
}
