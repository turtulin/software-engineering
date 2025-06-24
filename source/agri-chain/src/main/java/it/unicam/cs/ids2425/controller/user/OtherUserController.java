package it.unicam.cs.ids2425.controller.user;

import it.unicam.cs.ids2425.controller.authentication.TokenController;
import it.unicam.cs.ids2425.controller.eshop.stock.StockController;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.UserRole;
import it.unicam.cs.ids2425.model.user.UserState;
import it.unicam.cs.ids2425.repository.user.UserRepository;
import it.unicam.cs.ids2425.repository.user.UserStateRepository;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.UserStatusCode;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OtherUserController extends UserController {
    private final List<UserRole> hasStock = List.of(UserRole.CUSTOMER, UserRole.PRODUCER, UserRole.TRANSFORMER, UserRole.DISTRIBUTOR, UserRole.EVENT_PLANNER);
    private final StockController stockController;

    public OtherUserController(UserRepository userRepository, TokenController tokenController, UserStateRepository userStateRepository, SingleEntityController singleEntityController, StockController stockController) {
        super(userRepository, tokenController, userStateRepository, singleEntityController);
        this.stockController = stockController;
    }

    @Override
    @Transactional
    public User register(@NonNull User user) {
        if (user.getRole() == UserRole.GUEST || user.getRole() == UserRole.TIME) {
            throw new IllegalArgumentException("User must not be a guest or time");
        }
        if (!userRepository.findAllByUsername(user.getUsername()).isEmpty()) {
            throw new IllegalArgumentException("Username already exists");
        }
        UserStatusCode statusCode = user.getRole() == UserRole.CUSTOMER ? UserStatusCode.ACTIVE : UserStatusCode.PENDING;
        UserState state = new UserState(statusCode, user, "User registered", user, null);
        if (hasStock.contains(user.getRole())) {
            stockController.createStock(user);
        }
        userRepository.save(user);
        userStateRepository.save(state);
        return userRepository.findById(user.getId()).orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
