package it.unicam.cs.ids2425.users.model;


import it.unicam.cs.ids2425.core.identifiers.Identifiable;

public interface IUser extends Identifiable<Long> {
    UserRole getRole();

    String getUsername();
}
