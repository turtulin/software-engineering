package it.unicam.cs.ids2425.users.controller.actors;

import it.unicam.cs.ids2425.users.controller.CanLogoutController;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.actors.Admin;
import it.unicam.cs.ids2425.utilities.statuses.StatusInfo;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.wrappers.Pair;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
public class AdminController extends GenericUserController implements CanRegisterController, CanLogoutController {
    @Override
    public IUser register(IUser u) {
        if (super.get(u, null) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        if (u.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User role is not valid");
        }
        Admin a = (Admin) u;
        userRepository.create(a);
        userStatusRepository.create(new Pair<>(a,
                List.of(statusInfoController.create(new StatusInfo<>(UserStatus.PENDING, a), a))));
        return super.get(a, UserStatus.PENDING);
    }

    @Override
    protected boolean check(@NonNull IUser u, UserStatus status) {
        IUser user = super.get(u, null);
        if (!super.check(u, status) && user.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("User is not an Admin");
        }
        return true;
    }

    public IUser ban(IUser user) {
        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("Admin cannot be banned");
        }
        user = super.get(user, UserStatus.ACTIVE);
        StatusInfo<UserStatus> statusInfo = statusInfoController.create(new StatusInfo<>(UserStatus.BANNED, user), user);
        Pair<IUser, List<StatusInfo<UserStatus>>> pair = userStatusRepository.get(user);
        pair.getValue().add(statusInfo);
        userStatusRepository.save(user, pair);
        return super.get(user, UserStatus.BANNED);
    }

    public IUser unban(IUser user) {
        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("Admin cannot be banned");
        }
        user = super.get(user, UserStatus.BANNED);
        StatusInfo<UserStatus> statusInfo = statusInfoController.create(new StatusInfo<>(UserStatus.ACTIVE, user), user);
        Pair<IUser, List<StatusInfo<UserStatus>>> pair = userStatusRepository.get(user);
        pair.getValue().add(statusInfo);
        userStatusRepository.save(user, pair);
        return super.get(user, UserStatus.ACTIVE);
    }

    public IUser activate(IUser user) {
        user = super.get(user, null);
        StatusInfo<UserStatus> statusInfo = statusInfoController.create(new StatusInfo<>(UserStatus.ACTIVE, user), user);
        Pair<IUser, List<StatusInfo<UserStatus>>> pair = userStatusRepository.get(user);
        pair.getValue().add(statusInfo);
        userStatusRepository.save(user, pair);
        return super.get(user, UserStatus.ACTIVE);
    }

    // TODO: consider cron to reenable user!
    public IUser deactivate(IUser user, String reason, Timestamp until) {
        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("Admin cannot be banned");
        }
        user = super.get(user, UserStatus.ACTIVE);
        StatusInfo<UserStatus> statusInfo = statusInfoController.create(new StatusInfo<>(UserStatus.DEACTIVATED, user, reason, until), user);
        Pair<IUser, List<StatusInfo<UserStatus>>> pair = userStatusRepository.get(user);
        pair.getValue().add(statusInfo);
        userStatusRepository.save(user, pair);
        return super.get(user, UserStatus.DEACTIVATED);
    }
}
