package it.unicam.cs.ids2425.users.view.actors;

import it.unicam.cs.ids2425.problems.model.IProblem;
import it.unicam.cs.ids2425.problems.model.problems.ArticleProblem;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.Customer;
import it.unicam.cs.ids2425.users.model.actors.CustomerService;
import it.unicam.cs.ids2425.users.view.GenericUserViewTest;
import it.unicam.cs.ids2425.utilities.statuses.ProblemStatus;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceViewTest extends GenericUserViewTest {
    @Test
    void register() {
        CustomerService newCustomerService = new CustomerService("newCustomerService", "password");
        ViewResponse<IUser> response = customerServiceView.register(newCustomerService);
        assertAll(
                () -> assertEquals(ResponseStatus.CREATED, response.getStatus()),
                () -> assertNotNull(response.getData().getId()),
                () -> assertEquals("newCustomerService", response.getData().getUsername())
        );
        assertNotNull(adminView.getUser(admin, response.getData(), UserStatus.PENDING).getData());
    }

    @Test
    void getAll() {
        ViewResponse<List<IProblem>> response = customerServiceView.getAll(null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        response = customerServiceView.getAll(customerService);
        assertEquals(ResponseStatus.NOT_FOUND, response.getStatus());
        assertNull(response.getData());
        assertEquals("No result found", response.getMessage());

        List<IProblem> problems = addProblems();

        response = customerServiceView.getAll(customerService);
        assertEquals(ResponseStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(problems.size(), response.getData().size());
        assertNull(response.getMessage());
        assertTrue(response.getData().containsAll(problems));
        assertTrue(problems.containsAll(response.getData()));
    }

    @Test
    void get() {
        ViewResponse<IProblem> response = customerServiceView.get(null, new Customer("", ""));
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("problem is marked non-null but is null", response.getMessage());

        response = customerServiceView.get(new ArticleProblem<>(null, null, null), null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        List<IProblem> problems = addProblems();

        for (IProblem p : problems) {
            response = customerServiceView.get(p, customerService);
            assertEquals(ResponseStatus.OK, response.getStatus());
            assertNotNull(response.getData());
            assertEquals(p, response.getData());
            assertNull(response.getMessage());
        }
    }

    @Test
    void solve() {
        ViewResponse<IProblem> response = customerServiceView.solve(null, new Customer("", ""));
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("problem is marked non-null but is null", response.getMessage());

        response = customerServiceView.solve(new ArticleProblem<>(null, null, null), null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        List<IProblem> problems = addProblems();

        for (IProblem p : problems) {
            response = customerServiceView.solve(p, customerService);
            assertEquals(ResponseStatus.OK, response.getStatus());
            assertNotNull(response.getData());
            assertEquals(p, response.getData());
            assertNull(response.getMessage());
            assertEquals(ProblemStatus.RESOLVED, customerServiceView.getStatus(p, customerService).getData());
        }
    }

    @Test
    void reject() {

        ViewResponse<IProblem> response = customerServiceView.reject(null, new Customer("", ""));
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("problem is marked non-null but is null", response.getMessage());

        response = customerServiceView.reject(new ArticleProblem<>(null, null, null), null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        List<IProblem> problems = addProblems();

        for (IProblem p : problems) {
            response = customerServiceView.reject(p, customerService);
            assertEquals(ResponseStatus.OK, response.getStatus());
            assertNotNull(response.getData());
            assertEquals(p, response.getData());
            assertNull(response.getMessage());
            assertEquals(ProblemStatus.UNRESOLVED, customerServiceView.getStatus(p, customerService).getData());
        }
    }

    @Test
    void close() {
        ViewResponse<IProblem> response = customerServiceView.close(null, new Customer("", ""));
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("problem is marked non-null but is null", response.getMessage());

        response = customerServiceView.close(new ArticleProblem<>(null, null, null), null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        List<IProblem> problems = addProblems();

        for (IProblem p : problems) {
            response = customerServiceView.close(p, customerService);
            assertEquals(ResponseStatus.OK, response.getStatus());
            assertNotNull(response.getData());
            assertEquals(p, response.getData());
            assertNull(response.getMessage());
            assertEquals(ProblemStatus.CLOSED, customerServiceView.getStatus(p, customerService).getData());
        }
    }
}