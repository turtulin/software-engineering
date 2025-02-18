package it.unicam.cs.ids2425.users.model.actors;

import it.unicam.cs.ids2425.eshop.model.Cart;
import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true, exclude = "cart")
@Data
@ToString(callSuper = true)
public class Customer extends GenericUser {
    private Cart cart;

    public Customer(String username, String password) {
        super(username, password);
        this.cart = new Cart(this);
    }

    @Override
    public UserRole getRole() {
        return UserRole.CUSTOMER;
    }
}
