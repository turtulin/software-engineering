package it.unicam.cs.ids2425.users.controller.actors;

import it.unicam.cs.ids2425.users.controller.CanLogoutController;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.UserState;
import it.unicam.cs.ids2425.users.model.actors.Admin;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;

@NoArgsConstructor
public class AdminController extends GenericUserController implements CanRegisterController, CanLogoutController {
    public List<IUser> getAll(@NonNull IUser user) {
        check(user, UserStatus.ACTIVE);
        return userRepository.findAll().stream().toList();
    }

    public IUser get(IUser user, IUser target, UserStatus status) {
        check(user, UserStatus.ACTIVE);
        return super.get(target, status);
    }

    public UserState getUserState(IUser user, IUser target) {
        check(user, UserStatus.ACTIVE);
        return userStatusRepository.findAll().stream()
                .filter(s -> s.getEntity().equals(target)).sorted().toList().getLast();
    }

    @Override
    public IUser register(IUser u) {
        try {
            super.get(u, null);
            throw new IllegalArgumentException("User already exists");
        } catch (NoSuchElementException ignored) {
        }
        if (u.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User role is not valid");
        }
        Admin a = (Admin) u;
        // TODO review, setting admin as active automatically for testing purposes
        UserState userStatus = new UserState(a, UserStatus.ACTIVE, null);

        userRepository.save(a);
        userStatusRepository.save(userStatus);
        // TODO review, same as above
        return super.get(a, UserStatus.ACTIVE);
    }

    @Override
    protected boolean check(@NonNull IUser user, UserStatus status) {
        IUser U = super.get(user, null);
        if (!super.check(U, status) && U.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User is not an Admin");
        }
        return true;
    }

    private IUser changeUserStatus(IUser user, IUser target, UserStatus previousStatus, UserStatus newStatus) {
        user = get(user, UserStatus.ACTIVE);
        if (target.getRole() == UserRole.ADMIN && newStatus != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("Admin cannot be banned");
        }
        IUser t = super.get(target, previousStatus);
        UserState oldState = userStatusRepository.findAll().stream()
                .filter(s -> s.getEntity().equals(t))
                .filter(s -> previousStatus == null || s.getStatus() == previousStatus)
                .findFirst().orElseThrow(() -> new NoSuchElementException("User status not found"));
        UserState userStatus = new UserState(t, newStatus, user, oldState);
        userStatusRepository.save(userStatus);
        return super.get(t, newStatus);
    }

    public IUser ban(@NonNull IUser user, @NonNull IUser target) {
        return changeUserStatus(user, target, UserStatus.ACTIVE, UserStatus.BANNED);
    }

    public IUser unban(@NonNull IUser user, @NonNull IUser target) {
        return changeUserStatus(user, target, UserStatus.BANNED, UserStatus.ACTIVE);
    }

    public IUser activate(@NonNull IUser user, @NonNull IUser target) {
        return changeUserStatus(user, target, null, UserStatus.ACTIVE);
    }

    public IUser deactivate(@NonNull IUser user, @NonNull IUser target, @NonNull String reason, @NonNull Timestamp until) {
        user = get(user, UserStatus.ACTIVE);
        if (target.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("Admin cannot be banned");
        }
        if (reason.isBlank()) {
            throw new IllegalArgumentException("Reason is blank");
        }
        if (until.before(new Timestamp(System.currentTimeMillis())) || until.equals(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Until date is in the past");
        }
        IUser t = super.get(target, null);
        UserState oldState = userStatusRepository.findAll().stream()
                .filter(s -> s.getEntity().equals(t)).sorted().toList().getLast();
        UserState userStatus = new UserState(t, UserStatus.DEACTIVATED, user, reason, oldState, until);
        userStatusRepository.save(userStatus);
        return super.get(t, UserStatus.DEACTIVATED);
    }
}
