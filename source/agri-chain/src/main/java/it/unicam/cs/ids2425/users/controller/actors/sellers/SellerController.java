package it.unicam.cs.ids2425.users.controller.actors.sellers;

import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.actors.sellers.GenericSeller;
import it.unicam.cs.ids2425.utilities.statuses.StatusInfo;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.wrappers.Pair;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
public class SellerController extends GenericUserController implements CanRegisterController {
    private final List<UserRole> sellerRoles = List.of(UserRole.DISTRIBUTOR, UserRole.PRODUCER, UserRole.TRANSFORMER, UserRole.EVENT_PLANNER);

    @Override
    protected boolean check(@NonNull IUser u, UserStatus status) {
        u = super.get(u, null);
        if (super.check(u, status) && (sellerRoles.contains(u.getRole()) || List.of(UserRole.CUSTOMER_SERVICE, UserRole.ADMIN).contains(u.getRole()))) {
            return true;
        }
        throw new IllegalArgumentException("User role is not valid");
    }

    @Override
    public IUser register(@NonNull IUser u) {
        if (super.get(u, null) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        if (!sellerRoles.contains(u.getRole())) {
            throw new IllegalArgumentException("User role is not valid");
        }
        GenericSeller s = (GenericSeller) u;
        userRepository.create(s);
        userStatusRepository.create(new Pair<>(s,
                List.of(statusInfoController.create(new StatusInfo<>(UserStatus.PENDING, s), s))));
        return super.get(s, UserStatus.PENDING);
    }
}
