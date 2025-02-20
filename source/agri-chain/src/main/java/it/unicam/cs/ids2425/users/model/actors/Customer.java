package it.unicam.cs.ids2425.users.model.actors;

import it.unicam.cs.ids2425.eshop.model.stocks.Cart;
import it.unicam.cs.ids2425.eshop.model.stocks.GenericStock;
import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.IStockUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true, exclude = "stock")
@Data
@ToString(callSuper = true)
public class Customer extends GenericUser implements IStockUser {
    private Cart stock;

    public Customer(String username, String password) {
        super(username, password);
        this.stock = new Cart(this);
    }

    @Override
    public UserRole getRole() {
        return UserRole.CUSTOMER;
    }

    @Override
    public void setStock(GenericStock<?, ?> stock) {
        this.stock = (Cart) stock;
    }
}
