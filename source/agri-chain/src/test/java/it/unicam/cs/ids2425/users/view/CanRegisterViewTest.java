package it.unicam.cs.ids2425.users.view;

import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.actors.sellers.EventPlanner;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanRegisterViewTest extends GenericUserViewTest {
    @Test
    void register() {
        ViewResponse<IUser> response = sellerView.register(null);
        assertNotNull(response);
        assertNotNull(response.getStatus());
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("user is marked non-null but is null", response.getMessage());

        ISeller newSeller = new EventPlanner("newEventPlanner", "password");
        response = sellerView.register(newSeller);
        assertEquals(ResponseStatus.CREATED, response.getStatus());
        assertNotNull(response.getData().getId());

        response = customerView.register(response.getData());
        assertEquals(ResponseStatus.BAD_REQUEST, response.getStatus());
        assertNull(response.getData());
        assertEquals("User already exists", response.getMessage());
    }
}