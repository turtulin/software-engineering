package it.unicam.cs.ids2425.problems.model;

import it.unicam.cs.ids2425.users.model.IUser;

public interface IProblem {
    String getDescription();
    IUser getUser();
    void setUser(IUser user);
}
