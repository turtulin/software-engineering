package it.unicam.cs.ids2425.user.view;

import it.unicam.cs.ids2425.user.controller.actor.AdminController;
import it.unicam.cs.ids2425.user.controller.actor.OtherUserController;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserState;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.UserStatusCode;
import it.unicam.cs.ids2425.utilities.view.IView;
import it.unicam.cs.ids2425.utilities.view.ViewResponse;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Getter
@RestController
@RequestMapping("/admin")
public class AdminView implements IView, ICanLogoutView, ICanRegisterView {
    private final OtherUserController logoutController;
    private final AdminController registerController;

    private AdminView(OtherUserController logoutController, AdminController registerController) {
        this.logoutController = logoutController;
        this.registerController = registerController;
    }

    @RequestMapping(value = "/all", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<User>>> getAllUsers(@RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.getAllUsers(admin));
    }

    @RequestMapping(value = "/all/{status}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<List<User>>> getAllUsers(@PathVariable String status,
                                                                @RequestAttribute("user") User admin) {
        UserStatusCode statusCode = UserStatusCode.valueOf(status.toUpperCase());
        return genericCall(() -> registerController.getAllUsers(statusCode, admin));
    }

    @RequestMapping(value = "/{id}/{status}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<User>> getUserById(@PathVariable Long id,
                                                          @PathVariable String status,
                                                          @RequestAttribute("user") User admin) {
        UserStatusCode statusCode = UserStatusCode.valueOf(status.toUpperCase());
        return genericCall(() -> registerController.getUserById(id, statusCode, admin));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<User>> getUserById(@PathVariable Long id,
                                                          @RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.getUserById(id, null, admin));
    }

    @RequestMapping(value = "/{id}/state", method = {RequestMethod.GET})
    public ResponseEntity<ViewResponse<UserState>> getUserStatus(@PathVariable Long id,
                                                                 @RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.getLastUserState(id, admin));
    }

    @RequestMapping(value = "/{id}/ban", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<UserState>> banUser(@PathVariable Long id,
                                                           @RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.updateUser(id, UserStatusCode.BANNED, admin));
    }

    @RequestMapping(value = "/{id}/unban", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<UserState>> unbanUser(@PathVariable Long id,
                                                             @RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.updateUser(id, UserStatusCode.ACTIVE, admin));
    }

    // Improve by adding timestamp to the state so user is inactive until a future datetime
    @RequestMapping(value = "/{id}/deactivate", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<UserState>> deactivateUser(@PathVariable Long id,
                                                                  @RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.updateUser(id, UserStatusCode.INACTIVE, admin));
    }

    @RequestMapping(value = "/{id}/activate", method = {RequestMethod.PUT})
    public ResponseEntity<ViewResponse<UserState>> activateUser(@PathVariable Long id,
                                                                @RequestAttribute("user") User admin) {
        return genericCall(() -> registerController.updateUser(id, UserStatusCode.ACTIVE, admin));
    }
}
