package it.unicam.cs.ids2425.users.controller.actors;

import it.unicam.cs.ids2425.users.controller.CanLogoutController;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.actors.CustomerService;
import it.unicam.cs.ids2425.utilities.statuses.StatusInfo;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.wrappers.Pair;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
public class CustomerServiceController extends GenericUserController implements CanRegisterController, CanLogoutController {
    @Override
    public IUser register(IUser u) {
        if (super.get(u, null) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        if (u.getRole() != UserRole.CUSTOMER_SERVICE) {
            throw new IllegalArgumentException("User role is not valid");
        }
        CustomerService cs = (CustomerService) u;
        userRepository.create(cs);
        userStatusRepository.create(new Pair<>(cs,
                List.of(statusInfoController.create(new StatusInfo<>(UserStatus.PENDING, cs), cs))));
        return super.get(cs, UserStatus.PENDING);
    }

    @Override
    protected boolean check(@NonNull IUser u, UserStatus status) {
        IUser user = super.get(u, null);
        if (!super.check(u, status) && List.of(UserRole.CUSTOMER_SERVICE, UserRole.ADMIN).contains(user.getRole())) {
            throw new IllegalArgumentException("User is not a Customer Service");
        }
        return true;
    }
}
