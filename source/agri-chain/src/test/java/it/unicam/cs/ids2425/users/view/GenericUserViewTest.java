package it.unicam.cs.ids2425.users.view;

import it.unicam.cs.ids2425.articles.model.articles.Event;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.problems.model.IProblem;
import it.unicam.cs.ids2425.problems.model.problems.ArticleProblem;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.Admin;
import it.unicam.cs.ids2425.users.model.actors.Customer;
import it.unicam.cs.ids2425.users.model.actors.CustomerService;
import it.unicam.cs.ids2425.users.model.actors.Moderator;
import it.unicam.cs.ids2425.users.model.actors.sellers.EventPlanner;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import it.unicam.cs.ids2425.users.view.actors.AdminView;
import it.unicam.cs.ids2425.users.view.actors.CustomerServiceView;
import it.unicam.cs.ids2425.users.view.actors.CustomerView;
import it.unicam.cs.ids2425.users.view.actors.ModeratorView;
import it.unicam.cs.ids2425.users.view.actors.sellers.SellerView;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.views.SingletonView;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GenericUserViewTest {
    protected static Random r;
    protected static AdminView adminView;
    protected static CustomerServiceView customerServiceView;
    protected static ModeratorView moderatorView;
    protected static SellerView sellerView;
    protected static CustomerView customerView;
    protected static GenericUserView genericUserView;
    protected static Admin admin;
    protected static CustomerService customerService;
    protected static Moderator moderator;
    protected static ISeller seller;

    protected static List<IArticle> addArticles() {
        return addArticles(100);
    }

    protected static List<IArticle> addArticles(int n) {
        assertTrue(n > 0);
        List<IArticle> articles = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            IArticle a = new Event("nome " + i, "desc " + i, seller, r.nextDouble(100));
            ViewResponse<IArticle> viewResponse = sellerView.create(a, seller);
            if (viewResponse.getStatus() == ResponseStatus.CREATED) {
                viewResponse = sellerView.publishArticle(viewResponse.getData(), seller);
                if (viewResponse.getStatus() == ResponseStatus.ACCEPTED) {
                    ViewResponse<ArticleStatus> stateViewResponse = moderatorView.approve(viewResponse.getData(), moderator);
                    if (stateViewResponse.getStatus() == ResponseStatus.ACCEPTED) {
                        articles.add(viewResponse.getData());
                    }
                }
            }
        }
        return articles;
    }

    protected static List<IUser> addUsers(int n) {
        assertTrue(n > 0);
        List<IUser> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            IUser a = new Customer("nome " + i, "password");
            ViewResponse<IUser> viewResponse = customerView.register(a);
            if (viewResponse.getStatus() == ResponseStatus.CREATED) {
                users.add(viewResponse.getData());
            }
        }
        return users;
    }

    protected static List<IProblem> addProblems() {
        return addProblems(100);
    }

    protected static List<IProblem> addProblems(int n) {
        List<IArticle> articles = customerView.getAll().getData();
        if (articles == null || articles.isEmpty()) {
            addArticles(1);
            articles = customerView.getAll().getData();
        }
        List<IUser> users = adminView.getUsers(admin).getData();
        if (users == null || users.isEmpty()) {
            addUsers(1);
            users = adminView.getUsers(admin).getData();
        }
        List<IProblem> problems = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            IProblem a = new ArticleProblem<>("nome " + i, articles.getFirst(), users.getFirst());
            ViewResponse<IProblem> viewResponse = customerView.reportProblem(a, users.getFirst());
            if (viewResponse.getStatus() == ResponseStatus.CREATED) {
                problems.add(viewResponse.getData());
            }
        }
        return problems;
    }

    @BeforeEach
    protected void setUp() {
        adminView = SingletonView.getInstance(new AdminView());
        customerServiceView = SingletonView.getInstance(new CustomerServiceView());
        moderatorView = SingletonView.getInstance(new ModeratorView());
        sellerView = SingletonView.getInstance(new SellerView());
        customerView = SingletonView.getInstance(new CustomerView());
        genericUserView = SingletonView.getInstance(new GenericUserView());


        admin = new Admin("mockAdmin", "password");
        customerService = new CustomerService("mockCustomerService", "password");
        moderator = new Moderator("mockModerator", "password");
        seller = new EventPlanner("mockEventPlanner", "password");

        admin = (Admin) adminView.register(admin).getData();
        customerService = (CustomerService) customerServiceView.register(customerService).getData();
        moderator = (Moderator) moderatorView.register(moderator).getData();
        seller = (ISeller) sellerView.register(seller).getData();

        customerService = (CustomerService) adminView.activate(admin, customerService).getData();
        moderator = (Moderator) adminView.activate(admin, moderator).getData();
        seller = (ISeller) adminView.activate(admin, seller).getData();

        r = new Random(42);
    }

    @AfterEach
    protected void cleanData() {
        SingletonRepository.getInstance(IArticle.class).deleteAll();
        SingletonRepository.getInstance(IUser.class).deleteAll();
        SingletonRepository.getInstance(IProblem.class).deleteAll();
    }

    @Test
    void getAll() {
        // null datum
        assertNotNull(genericUserView.getAll());
        assertNotNull(genericUserView.getAll().getStatus());
        assertEquals(ResponseStatus.NOT_FOUND, genericUserView.getAll().getStatus());
        assertNull(genericUserView.getAll().getData());
        assertEquals("No result found", genericUserView.getAll().getMessage());

        // add data
        List<IArticle> data = addArticles();

        assertFalse(data.isEmpty());

        assertNotNull(genericUserView.getAll().getStatus());
        assertEquals(ResponseStatus.OK, genericUserView.getAll().getStatus());
        assertNotNull(genericUserView.getAll().getData());
        assertFalse(genericUserView.getAll().getData().isEmpty());
        assertNull(genericUserView.getAll().getMessage());
        assertTrue(data.containsAll(genericUserView.getAll().getData()));
        assertTrue(genericUserView.getAll().getData().containsAll(data));
    }

    @Test
    void get() {
        // null datum
        assertNotNull(genericUserView.get(null));
        assertNotNull(genericUserView.get(null).getStatus());
        assertEquals(ResponseStatus.BAD_REQUEST, genericUserView.get(null).getStatus());
        assertNull(genericUserView.get(null).getData());
        assertEquals("article is marked non-null but is null", genericUserView.get(null).getMessage());


        // non existent datum
        IArticle nonExistentDatum = new Event("fake", "fake desc", new EventPlanner("fakeEventPlanner1", "password"), 0);
        assertNotNull(genericUserView.get(nonExistentDatum).getStatus());
        assertEquals(ResponseStatus.BAD_REQUEST, genericUserView.get(nonExistentDatum).getStatus());
        assertNull(genericUserView.get(nonExistentDatum).getData());
        assertEquals("Article not found", genericUserView.get(nonExistentDatum).getMessage());

        // add data
        List<IArticle> data = addArticles();

        assertFalse(data.isEmpty());

        for (IArticle datum : data) {
            assertNotNull(genericUserView.get(datum).getStatus());
            assertEquals(ResponseStatus.OK, genericUserView.get(datum).getStatus());
            assertNotNull(genericUserView.get(datum).getData());
            assertNull(genericUserView.get(datum).getMessage());
            assertEquals(datum, genericUserView.get(datum).getData());
        }

        // non existent datum
        nonExistentDatum = new Event("fake2", "fake desc2", new EventPlanner("fakeEventPlanner2", "password"), 2);
        assertNotNull(genericUserView.get(nonExistentDatum).getStatus());
        assertEquals(ResponseStatus.BAD_REQUEST, genericUserView.get(nonExistentDatum).getStatus());
        assertNull(genericUserView.get(nonExistentDatum).getData());
        assertEquals("Article not found", genericUserView.get(nonExistentDatum).getMessage());
    }

    @Test
    void shareArticle() {
        // null datum
        assertNotNull(genericUserView.shareArticle(null));
        assertNotNull(genericUserView.shareArticle(null).getStatus());
        assertEquals(ResponseStatus.BAD_REQUEST, genericUserView.shareArticle(null).getStatus());
        assertNull(genericUserView.shareArticle(null).getData());
        assertEquals("article is marked non-null but is null", genericUserView.shareArticle(null).getMessage());


        // non existent datum
        IArticle nonExistentDatum = new Event("fake2", "fake desc2", new EventPlanner("fakeEventPlanner1", "password"), 2);
        assertNotNull(genericUserView.shareArticle(nonExistentDatum).getStatus());
        assertEquals(ResponseStatus.BAD_REQUEST, genericUserView.shareArticle(nonExistentDatum).getStatus());
        assertNull(genericUserView.shareArticle(nonExistentDatum).getData());
        assertEquals("Article not found", genericUserView.shareArticle(nonExistentDatum).getMessage());

        // add data
        List<IArticle> data = addArticles();

        assertFalse(data.isEmpty());

        for (IArticle datum : data) {
            assertNotNull(genericUserView.shareArticle(datum).getStatus());
            assertEquals(ResponseStatus.OK, genericUserView.shareArticle(datum).getStatus());
            assertNotNull(genericUserView.shareArticle(datum).getData());
            assertNull(genericUserView.shareArticle(datum).getMessage());
            assertEquals(datum.toString(), genericUserView.shareArticle(datum).getData());
        }

        // non existent datum
        nonExistentDatum = new Event("fake2", "fake desc2", new EventPlanner("fakeEventPlanner2", "password"), 2);
        assertNotNull(genericUserView.shareArticle(nonExistentDatum).getStatus());
        assertEquals(ResponseStatus.BAD_REQUEST, genericUserView.shareArticle(nonExistentDatum).getStatus());
        assertNull(genericUserView.shareArticle(nonExistentDatum).getData());
        assertEquals("Article not found", genericUserView.shareArticle(nonExistentDatum).getMessage());
    }
}