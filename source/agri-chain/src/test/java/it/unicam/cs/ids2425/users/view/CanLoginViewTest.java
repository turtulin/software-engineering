package it.unicam.cs.ids2425.users.view;

import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.Customer;
import it.unicam.cs.ids2425.users.model.actors.sellers.EventPlanner;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanLoginViewTest extends GenericUserViewTest {

    @Test
    void login() {
        ViewResponse<Token> response = customerView.login(null, null);
        assertNotNull(response);
        assertNotNull(response.getStatus());
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("username is marked non-null but is null", response.getMessage());


        response = customerView.login("nonExistingUser", "nonExistingPassword");
        assertNotNull(response);
        assertNotNull(response.getStatus());
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("User not found", response.getMessage());

        Customer newUser = new Customer("newCustomer", "password");
        IUser user = customerView.register(newUser).getData();
        response = customerView.login(user.getUsername(), ((Customer) user).getPassword());
        assertNotNull(response);
        assertNotNull(response.getStatus());
        assertEquals(ResponseStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertNull(response.getMessage());
        assertEquals(user, response.getData().getUser());

        ISeller seller = new EventPlanner("newEventPlanner", "password");
        seller = (ISeller) sellerView.register(seller).getData();
        response = customerView.login(seller.getUsername(), ((EventPlanner) seller).getPassword());
        assertNotNull(response);
        assertNotNull(response.getStatus());
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("User is not active", response.getMessage());
    }
}