package it.unicam.cs.ids2425.users.model.actors;

import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.UserRole;

public class Admin extends GenericUser {
    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }
}
