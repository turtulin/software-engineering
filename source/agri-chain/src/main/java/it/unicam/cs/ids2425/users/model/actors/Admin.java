package it.unicam.cs.ids2425.users.model.actors;

import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.UserRole;

public class Admin extends GenericUser {
    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }
}
