package it.unicam.cs.ids2425.user.view;

import it.unicam.cs.ids2425.user.controller.actor.UserController;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface ICanLogoutView extends IView {
    UserController getLogoutController();

    @RequestMapping(path = "/logout", method = {RequestMethod.GET})
    default ResponseEntity<ViewResponse<String>> logout(@RequestAttribute("user") User user) {
        if (user.getRole() != UserRole.GUEST) {
            getLogoutController().logout(user);
        }
        return ResponseEntity.ok(ViewResponse.<String>builder().message("Logout successful").build());
    }

}
