package it.unicam.cs.ids2425.users.model.actors;

import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends GenericUser {
    @Override
    public UserRole getRole() {
        return UserRole.CUSTOMER;
    }
}
