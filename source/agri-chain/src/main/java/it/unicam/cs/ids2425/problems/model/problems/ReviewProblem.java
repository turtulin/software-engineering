package it.unicam.cs.ids2425.problems.model.problems;

import it.unicam.cs.ids2425.eshop.model.reviews.Review;
import it.unicam.cs.ids2425.problems.model.GenericProblem;
import it.unicam.cs.ids2425.users.model.IUser;

public class ReviewProblem extends GenericProblem<Review> {
    public ReviewProblem(String description, Review problem, IUser user) {
        super(description, problem, user);
    }
}
