package it.unicam.cs.ids2425.users.controller;

import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.UserState;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.NoSuchElementException;
import java.util.Optional;

@NoArgsConstructor
public class GenericUserController implements IController, CanLoginController {
    protected final SingletonRepository<IUser> userRepository = SingletonRepository.getInstance(IUser.class);
    protected final SingletonRepository<UserState> userStatusRepository = SingletonRepository.getInstance(UserState.class);

    public IUser get(@NonNull IUser user, UserStatus status) {
        if (status != null) {
            check(user, status);
        }
        Optional<IUser> u = userRepository.findById(user);
        if (u.isPresent()) {
            return u.get();
        }
        throw new NoSuchElementException("User not found");
    }

    protected boolean check(@NonNull IUser user, UserStatus status) {
        if (user.getRole() == UserRole.ADMIN) {
            // TODO: remove, only used for testing
            return true;
        }
        if (user.getRole() == UserRole.TIME) {
            // TODO: Implement this
            return true;
        }
        userStatusRepository.findAll().stream()
                .filter(s -> s.getEntity().equals(user))
                .filter(s -> s.getStatus() == status)
                .findFirst().orElseThrow(() -> new NoSuchElementException("User status not found"));
        return true;
    }

    @Override
    public SingletonRepository<IUser> getGenericUserRepository() {
        return userRepository;
    }

    @Override
    public SingletonRepository<UserState> getUserStatusRepository() {
        return userStatusRepository;
    }
}
