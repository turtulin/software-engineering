package it.unicam.cs.ids2425.users.model;

import it.unicam.cs.ids2425.articles.model.IArticle;
import it.unicam.cs.ids2425.problems.model.IProblem;

import java.util.List;

public interface IUser {
    List<IArticle> viewArticles();
    void shareContent();
    IProblem reportProblem();
}
