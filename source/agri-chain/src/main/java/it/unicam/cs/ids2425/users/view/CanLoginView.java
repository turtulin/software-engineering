package it.unicam.cs.ids2425.users.view;

import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.users.controller.CanLoginController;
import it.unicam.cs.ids2425.utilities.views.IView;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;

public interface CanLoginView extends IView {
    CanLoginController getCanLoginController();

    default ViewResponse<Token> login(String username, String password) {
        return genericCall(() -> getCanLoginController().login(username, password));
    }
}
