package it.unicam.cs.ids2425.view;

import it.unicam.cs.ids2425.controller.user.UserController;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface ICanRegisterView extends IView {
    UserController getRegisterController();

    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    default ResponseEntity<ViewResponse<User>> register(@RequestBody User body) {
        return genericCall(() -> getRegisterController().register(body), HttpStatus.CREATED);
    }
}
