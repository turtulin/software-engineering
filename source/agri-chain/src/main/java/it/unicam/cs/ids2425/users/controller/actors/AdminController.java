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
    public List<IUser> getAll(IUser user) {
        check(user, UserStatus.ACTIVE);
        return userRepository.findAll().stream().toList();
    }

    @Override
    public IUser register(IUser u) {
        if (super.get(u, null) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        if (u.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User role is not valid");
        }
        Admin a = (Admin) u;
        UserState userStatus = new UserState(a, UserStatus.PENDING, null);

        userRepository.save(a);
        userStatusRepository.save(userStatus);
        return super.get(a, UserStatus.PENDING);
    }

    @Override
    protected boolean check(@NonNull IUser u, UserStatus status) {
        IUser user = super.get(u, null);
        if (!super.check(u, status) && user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User is not an Admin");
        }
        return true;
    }

    private IUser changeUserStatus(IUser user, IUser target, UserStatus previousStatus, UserStatus newStatus) {
        user = get(user, UserStatus.ACTIVE);
        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("Admin cannot be banned");
        }
        IUser t = super.get(target, previousStatus);
        UserState oldState = userStatusRepository.findAll().stream()
                .filter(s -> s.getEntity().equals(t))
                .filter(s -> s.getStatus() == previousStatus)
                .findFirst().orElseThrow(() -> new NoSuchElementException("User status not found"));
        UserState userStatus = new UserState(target, newStatus, user, oldState);
        userStatusRepository.save(userStatus);
        return super.get(user, newStatus);
    }

    public IUser ban(IUser user, IUser target) {
        return changeUserStatus(user, target, UserStatus.ACTIVE, UserStatus.BANNED);
    }

    public IUser unban(IUser user, IUser target) {
        return changeUserStatus(user, target, UserStatus.BANNED, UserStatus.ACTIVE);
    }

    public IUser activate(IUser user, IUser target) {
        return changeUserStatus(user, target, null, UserStatus.ACTIVE);
    }

    public IUser deactivate(IUser user, IUser target, String reason, Timestamp until) {
        user = get(user, UserStatus.ACTIVE);
        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("Admin cannot be banned");
        }
        IUser t = super.get(target, null);
        UserState oldState = userStatusRepository.findAll().stream()
                .filter(s -> s.getEntity().equals(t))
                .findFirst().orElseThrow(() -> new NoSuchElementException("User status not found"));
        UserState userStatus = new UserState(target, UserStatus.DEACTIVATED, user, reason, oldState, until);
        userStatusRepository.save(userStatus);
        return super.get(user, UserStatus.DEACTIVATED);
    }
}
