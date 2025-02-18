package it.unicam.cs.ids2425.users.view;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.problems.model.IProblem;
import it.unicam.cs.ids2425.problems.model.problems.ArticleProblem;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanReportViewTest extends GenericUserViewTest {
    @Test
    void reportProblem() {
        // null problem
        IUser customer = addUsers(1).getFirst();
        ViewResponse<?> response = customerView.reportProblem(null, customer);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("problem is marked non-null but is null", response.getMessage());

        // null user
        IProblem problem = new ArticleProblem<>("Defective product", null, null);
        response = customerView.reportProblem(problem, null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        // null problem description
        IProblem problem2 = new ArticleProblem<>(null, null, null);
        response = customerView.reportProblem(problem2, customer);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("Problem description is empty", response.getMessage());

        // blank problem description
        IProblem problem3 = new ArticleProblem<>("   ", null, null);
        response = customerView.reportProblem(problem3, customer);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("Problem description is empty", response.getMessage());

        // valid problem
        IArticle article = addArticles(1).getFirst();
        IProblem problem4 = new ArticleProblem<>("Defective product", article, customer);
        ViewResponse<?> response4 = customerView.reportProblem(problem4, customer);
        assertEquals(ResponseStatus.CREATED, response4.getStatus());
        assertNotNull(response4.getData());
        assertNull(response4.getMessage());
    }
}