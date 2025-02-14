package it.unicam.cs.ids2425.users.view;

import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.users.controller.CanLoginController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.views.IView;
import it.unicam.cs.ids2425.utilities.wrappers.Pair;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;

public interface CanLoginView extends IView {
    CanLoginController getCanLoginController();

    default ViewResponse<Pair<IUser, Token>> login(String username, String password) {
        return genericCall(() -> getCanLoginController().login(username, password));
    }
}
