package it.unicam.cs.ids2425.users.model.actors;

import it.unicam.cs.ids2425.users.model.UserRole;

public class Time extends Admin {
    @Override
    public UserRole getRole() {
        return UserRole.TIME;
    }
}
