package it.unicam.cs.ids2425.users.controller;

import it.unicam.cs.ids2425.authentication.controller.TokenController;
import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;

public interface CanLogoutController {
    default void logout(Token token) {
        SingletonController.getInstance(new TokenController() {
        }).remove(token);
    }
}
