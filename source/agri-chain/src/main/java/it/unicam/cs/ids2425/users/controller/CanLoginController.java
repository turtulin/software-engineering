package it.unicam.cs.ids2425.users.controller;

import it.unicam.cs.ids2425.authentication.controller.TokenController;
import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.StatusInfo;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.wrappers.Pair;

import java.util.List;
import java.util.Set;

public interface CanLoginController extends IController {
    SingletonRepository<List<IUser>, IUser, IUser> getGenericUserRepository();

    SingletonRepository<Set<Pair<IUser, List<StatusInfo<UserStatus>>>>, Pair<IUser, List<StatusInfo<UserStatus>>>, IUser> getUserStatusRepository();

    default Pair<IUser, Token> login(String username, String password) {
        IUser user = getGenericUserRepository().get(new GenericUser(username) {
        });
        if (user == null || !((GenericUser) user).getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        if (getUserStatusRepository().get(user).getValue().getLast().status() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("User is not active");
        }
        Token token = SingletonController.getInstance(new TokenController() {
        }).create(user);
        return new Pair<>(user, token);
    }
}
