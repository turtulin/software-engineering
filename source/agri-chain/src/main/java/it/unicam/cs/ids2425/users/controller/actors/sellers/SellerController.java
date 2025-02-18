package it.unicam.cs.ids2425.users.controller.actors.sellers;

import it.unicam.cs.ids2425.users.controller.CanLogoutController;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.UserState;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.NoSuchElementException;

@NoArgsConstructor
public class SellerController extends GenericUserController implements CanRegisterController, CanLogoutController {
    private final List<UserRole> sellerRoles = List.of(UserRole.DISTRIBUTOR, UserRole.PRODUCER, UserRole.TRANSFORMER, UserRole.EVENT_PLANNER);

    @Override
    protected boolean check(@NonNull IUser user, UserStatus status) {
        user = super.get(user, null);
        if (super.check(user, status) && (sellerRoles.contains(user.getRole()) || List.of(UserRole.CUSTOMER_SERVICE, UserRole.ADMIN).contains(user.getRole()))) {
            return true;
        }
        throw new IllegalArgumentException("User role is not valid");
    }

    @Override
    public IUser register(@NonNull IUser user) {
        try {
            super.get(user, null);
            throw new IllegalArgumentException("User already exists");
        } catch (NoSuchElementException ignored) {
        }
        if (!sellerRoles.contains(user.getRole())) {
            throw new IllegalArgumentException("User role is not valid");
        }
        ISeller s = (ISeller) user;
        UserState userStatus = new UserState(s, UserStatus.PENDING, null);

        userRepository.save(s);
        userStatusRepository.save(userStatus);
        return super.get(s, UserStatus.PENDING);
    }
}
