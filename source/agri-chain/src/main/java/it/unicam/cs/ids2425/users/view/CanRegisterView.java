package it.unicam.cs.ids2425.users.view;

import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.views.IView;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;

public interface CanRegisterView extends IView {
    CanRegisterController getCanRegisterController();

    default ViewResponse<IUser> register(IUser u) {
        return genericCall(() -> getCanRegisterController().register(u),
                ResponseStatus.CREATED,
                "User registered");
    }
}

