package it.unicam.cs.ids2425.users.view.actors;

import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.Admin;
import it.unicam.cs.ids2425.users.view.GenericUserViewTest;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminViewTest extends GenericUserViewTest {
    @Test
    void register() {
        Admin newAdmin = new Admin("newAdmin", "password");
        ViewResponse<IUser> response = adminView.register(newAdmin);
        assertAll(
                () -> assertEquals(ResponseStatus.CREATED, response.getStatus()),
                () -> assertNotNull(response.getData().getId()),
                () -> assertEquals("newAdmin", response.getData().getUsername())
        );
        String testing = System.getenv("testing");
        UserStatus status = testing != null && testing.equals("true") ? UserStatus.ACTIVE : UserStatus.PENDING;
        assertNotNull(adminView.getUser(admin, response.getData(), status).getData());
    }

    @Test
    void getUsers() {
        ViewResponse<List<IUser>> response = adminView.getUsers(null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        List<IUser> users = addUsers(10);
        response = adminView.getUsers(admin);
        assertEquals(ResponseStatus.OK, response.getStatus());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
        assertTrue(response.getData().size() >= users.size());
        assertTrue(response.getData().containsAll(users));
    }

    @Test
    void ban() {
        ViewResponse<IUser> response = adminView.ban(null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        response = adminView.ban(admin, null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("target is marked non-null but is null", response.getMessage());

        response = adminView.ban(admin, seller);
        assertEquals(ResponseStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(seller, response.getData());
        assertEquals(UserStatus.BANNED, adminView.getUserState(admin, seller).getData().getStatus());
    }

    @Test
    void unban() {
        ViewResponse<IUser> response = adminView.unban(null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        response = adminView.unban(admin, null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("target is marked non-null but is null", response.getMessage());

        adminView.ban(admin, seller);
        response = adminView.unban(admin, seller);
        assertEquals(ResponseStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(seller, response.getData());
        assertEquals(UserStatus.ACTIVE, adminView.getUserState(admin, seller).getData().getStatus());
    }

    @Test
    void deactivate() {
        ViewResponse<IUser> response = adminView.deactivate(null, null, null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        response = adminView.deactivate(admin, null, null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("target is marked non-null but is null", response.getMessage());

        response = adminView.deactivate(admin, seller, null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("reason is marked non-null but is null", response.getMessage());

        response = adminView.deactivate(admin, seller, "", null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("until is marked non-null but is null", response.getMessage());

        response = adminView.deactivate(admin, seller, "", new Timestamp(System.currentTimeMillis()));
        assertEquals(ResponseStatus.NOT_FOUND, response.getStatus());
        assertNull(response.getData());
        assertEquals("Reason is blank", response.getMessage());

        response = adminView.deactivate(admin, seller, "reason", new Timestamp(System.currentTimeMillis()));
        assertEquals(ResponseStatus.NOT_FOUND, response.getStatus());
        assertNull(response.getData());
        assertEquals("Until date is in the past", response.getMessage());

        response = adminView.deactivate(admin, seller, "reason", new Timestamp(System.currentTimeMillis() - 1000));
        assertEquals(ResponseStatus.NOT_FOUND, response.getStatus());
        assertNull(response.getData());
        assertEquals("Until date is in the past", response.getMessage());

        response = adminView.deactivate(admin, seller, "reason", new Timestamp(System.currentTimeMillis() + 1000));
        assertEquals(ResponseStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(adminView.getUser(admin, seller, UserStatus.DEACTIVATED).getData(), response.getData());
        assertEquals(UserStatus.DEACTIVATED, adminView.getUserState(admin, seller).getData().getStatus());
    }

    @Test
    void activate() {
        ViewResponse<IUser> response = adminView.activate(null, null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        response = adminView.activate(admin, null);
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("target is marked non-null but is null", response.getMessage());

        adminView.deactivate(admin, seller, "reason", new Timestamp(System.currentTimeMillis() + 1000));
        response = adminView.activate(admin, seller);
        assertEquals(ResponseStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(adminView.getUser(admin, seller, UserStatus.ACTIVE).getData(), response.getData());

        adminView.deactivate(admin, seller, "reason", new Timestamp(System.currentTimeMillis() + 1000));
        ViewResponse<Token> loginViewResponse = sellerView.login(seller.getUsername(), ((GenericUser) seller).getPassword());
        assertEquals(ResponseStatus.BAD_REQUEST, loginViewResponse.getStatus());
        assertNull(loginViewResponse.getData());
        assertEquals("User is not active", loginViewResponse.getMessage());

        try {
            Thread.sleep(1000);
            loginViewResponse = sellerView.login(seller.getUsername(), ((GenericUser) seller).getPassword());
            assertEquals(ResponseStatus.OK, loginViewResponse.getStatus());
            assertNotNull(loginViewResponse.getData());
            assertNull(loginViewResponse.getMessage());
            assertEquals(seller, loginViewResponse.getData().getUser());
        } catch (InterruptedException ignored) {
        }

    }
}