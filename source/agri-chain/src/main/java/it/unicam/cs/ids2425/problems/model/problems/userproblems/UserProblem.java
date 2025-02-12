package it.unicam.cs.ids2425.problems.model.problems.userproblems;

import it.unicam.cs.ids2425.problems.model.GenericProblem;
import it.unicam.cs.ids2425.users.model.IUser;

public class UserProblem<T extends IUser> extends GenericProblem<T> {
    public UserProblem(String description, T problem, IUser user) {
        super(description, problem, user);
    }
}
