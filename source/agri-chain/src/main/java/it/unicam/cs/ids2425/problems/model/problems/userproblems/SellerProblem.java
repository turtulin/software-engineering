package it.unicam.cs.ids2425.problems.model.problems.userproblems;

import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;

public class SellerProblem<T extends ISeller> extends UserProblem<T> {
    public SellerProblem(String description, T problem, IUser user) {
        super(description, problem, user);
    }
}
