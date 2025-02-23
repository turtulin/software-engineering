package it.unicam.cs.ids2425.user.view;

import com.fasterxml.jackson.databind.JsonNode;
import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.user.controller.actor.UserController;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface ICanLoginView extends IView {
    UserController getUserController();

    @RequestMapping(path = "/login", method = {RequestMethod.POST})
    default ResponseEntity<ViewResponse<Token>> login(@RequestBody JsonNode body) {
        String username = body.get("username").asText();
        String password = body.get("password").asText();
        return genericCall(() -> getUserController().login(username, password));
    }
}
