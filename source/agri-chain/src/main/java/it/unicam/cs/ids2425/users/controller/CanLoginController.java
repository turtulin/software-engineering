package it.unicam.cs.ids2425.users.controller;

import it.unicam.cs.ids2425.authentication.controller.TokenController;
import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserState;
import it.unicam.cs.ids2425.users.model.actors.Time;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;

import java.sql.Timestamp;
import java.util.NoSuchElementException;

public interface CanLoginController extends IController {
    SingletonRepository<IUser> getGenericUserRepository();

    SingletonRepository<UserState> getUserStatusRepository();

    default Token login(String username, String password) {
        IUser user = getGenericUserRepository().findAll().stream()
                .filter(u -> (u.getUsername().equals(username) &&
                        ((GenericUser) u).getPassword().equals(password)))
                .findFirst().orElseThrow(() -> new NoSuchElementException("User not found"));
        UserStatus userStatus = getUserStatusRepository().findAll().stream()
                .filter(s -> s.getEntity().equals(user))
                .sorted().toList().getLast().getStatus();
        if (userStatus == UserStatus.DEACTIVATED) {
            checkReenable(user);
        } else if (userStatus != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("User is not active");
        }
        return SingletonController.getInstance(new TokenController() {
        }).create(user);
    }

    private void checkReenable(IUser user) {
        // TODO: find a way to prevent ppl mocking time actor, as time will have same privileges as admin and will not be allowed to login.
        UserState userStatus = getUserStatusRepository().findAll().stream()
                .filter(s -> s.getEntity().equals(user))
                .sorted().toList().getLast();
        if (userStatus.getStatus() == UserStatus.DEACTIVATED &&
                new Timestamp(System.currentTimeMillis()).after(userStatus.getStateTime())) {
            UserState newState = new UserState(user, UserStatus.ACTIVE, new Time());
            getUserStatusRepository().save(newState);
            return;
        }
        throw new IllegalArgumentException("User is not active");
    }
}
