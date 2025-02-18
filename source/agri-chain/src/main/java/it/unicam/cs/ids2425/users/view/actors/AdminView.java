package it.unicam.cs.ids2425.users.view.actors;

import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.actors.AdminController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.view.CanRegisterView;
import it.unicam.cs.ids2425.users.view.GenericUserView;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
public class AdminView extends GenericUserView implements CanRegisterView {
    private final AdminController adminController = SingletonController.getInstance(new AdminController() {
    });

    public ViewResponse<List<IUser>> getUsers(IUser user) {
        return genericCall(() -> {
                    List<IUser> users = adminController.getAll(user);
                    return users.isEmpty() ? null : users;
                },
                ResponseStatus.OK,
                ResponseStatus.NOT_FOUND);
    }

    public ViewResponse<IUser> ban(IUser user, IUser target) {
        return genericCall(() -> adminController.ban(user, target), ResponseStatus.OK, ResponseStatus.NOT_FOUND);
    }

    public ViewResponse<IUser> unban(IUser user, IUser target) {
        return genericCall(() -> adminController.unban(user, target), ResponseStatus.OK, ResponseStatus.NOT_FOUND);
    }

    public ViewResponse<IUser> deactivate(IUser user, IUser target, String reason, Timestamp until) {
        return genericCall(() -> adminController.deactivate(user, target, reason, until), ResponseStatus.OK, ResponseStatus.NOT_FOUND);
    }

    public ViewResponse<IUser> activate(IUser user, IUser target) {
        return genericCall(() -> adminController.activate(user, target), ResponseStatus.OK, ResponseStatus.NOT_FOUND);
    }

    @Override
    public CanRegisterController getCanRegisterController() {
        return SingletonController.getInstance(new AdminController() {
        });
    }
}
