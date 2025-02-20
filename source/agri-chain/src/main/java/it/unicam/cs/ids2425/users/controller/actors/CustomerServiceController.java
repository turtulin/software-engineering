package it.unicam.cs.ids2425.users.controller.actors;

import it.unicam.cs.ids2425.users.controller.CanLogoutController;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.GenericUserController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.UserState;
import it.unicam.cs.ids2425.users.model.actors.CustomerService;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.NoSuchElementException;

@NoArgsConstructor
public class CustomerServiceController extends GenericUserController implements CanRegisterController, CanLogoutController {
    @Override
    public IUser register(IUser u) {
        try {
            super.get(u, null);
            throw new IllegalArgumentException("User already exists");
        } catch (NoSuchElementException ignored) {
        }

        if (u.getRole() != UserRole.CUSTOMER_SERVICE) {
            throw new IllegalArgumentException("User role is not valid");
        }
        CustomerService cs = (CustomerService) u;
        UserState userStatus = new UserState(cs, UserStatus.PENDING, null);

        userRepository.save(cs);
        userStatusRepository.save(userStatus);
        return super.get(cs, UserStatus.PENDING);
    }

    @Override
    protected boolean check(@NonNull IUser user, UserStatus status) {
        IUser u = super.get(user, null);
        if (!super.check(user, status) && List.of(UserRole.CUSTOMER_SERVICE, UserRole.ADMIN).contains(u.getRole())) {
            throw new IllegalArgumentException("User is not a Customer Service");
        }
        return true;
    }
}
