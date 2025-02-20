package it.unicam.cs.ids2425.users.model.actors;

import it.unicam.cs.ids2425.users.model.UserRole;

public class Time extends Admin {
    private static Time instance;

    private Time() {
        super();
        this.setUsername("time");
    }

    public static Time getInstance() {
        if (instance == null) {
            instance = new Time();
        }
        return instance;
    }

    @Override
    public UserRole getRole() {
        return UserRole.TIME;
    }
}
