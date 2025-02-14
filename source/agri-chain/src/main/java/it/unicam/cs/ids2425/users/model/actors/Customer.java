package it.unicam.cs.ids2425.users.model.actors;

import it.unicam.cs.ids2425.eshop.model.Cart;
import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class Customer extends GenericUser {
    private Cart cart;

    @Override
    public UserRole getRole() {
        return UserRole.CUSTOMER;
    }
}
