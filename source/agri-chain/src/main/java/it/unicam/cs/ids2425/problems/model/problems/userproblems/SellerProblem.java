package it.unicam.cs.ids2425.problems.model.problems.userproblems;

import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;

public class SellerProblem<T extends ISeller> extends UserProblem<T> {
    private T seller;
}
