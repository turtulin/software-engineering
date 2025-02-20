package it.unicam.cs.ids2425.problems.model.problems;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.problems.model.GenericProblem;
import it.unicam.cs.ids2425.users.model.IUser;

public class ArticleProblem<T extends IArticle> extends GenericProblem<T> {
    public ArticleProblem(String description, T problem, IUser user) {
        super(description, problem, user);
    }
}
