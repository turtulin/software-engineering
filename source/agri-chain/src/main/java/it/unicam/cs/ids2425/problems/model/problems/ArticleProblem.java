package it.unicam.cs.ids2425.problems.model.problems;

import it.unicam.cs.ids2425.articles.model.IArticle;
import it.unicam.cs.ids2425.problems.model.GenericProblem;

public class ArticleProblem<T extends IArticle> extends GenericProblem {
    private T article;
}
