package it.unicam.cs.ids2425.user.controller.actor;

import it.unicam.cs.ids2425.authentication.controller.TokenController;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import it.unicam.cs.ids2425.user.model.UserState;
import it.unicam.cs.ids2425.user.repository.UserRepository;
import it.unicam.cs.ids2425.user.repository.UserStateRepository;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.UserStatusCode;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminController extends UserController {
    private final TokenController tokenController;

    @Autowired
    public AdminController(UserRepository userRepository, UserStateRepository userStateRepository, SingleEntityController singleEntityController, TokenController tokenController) {
        super(userRepository, tokenController, userStateRepository, singleEntityController);
        this.tokenController = tokenController;
    }

    @Transactional
    @Override
    public User register(@NonNull User admin) {
        if (admin.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User must be an admin");
        }
        if (!userRepository.findAllByUsername(admin.getUsername()).isEmpty()) {
            throw new IllegalArgumentException("User already exists");
        }
        userRepository.save(admin);
        userStateRepository.save(new UserState(UserStatusCode.PENDING, admin, "User registered", admin, null));
        return userRepository.findById(admin.getId()).orElseThrow(() -> new IllegalStateException("User not found"));
    }

    public List<User> getAllUsers(@NonNull User admin) {
        checkAdmin(admin);
        return userRepository.findAll();
    }

    public User getUser(@NonNull Long id, UserStatusCode status, @NonNull User admin) {
        UserState state = getLastUserState(id, admin);
        if (status != null && !state.getStatusCode().equals(status)) {
            throw new IllegalStateException("User is not in the correct state");
        }
        return state.getEntity();
    }

    private void checkAdmin(@NonNull User admin) {
        UserState state = userStateRepository.findAllByEntity(admin).stream().sorted().toList().getLast();
        if (!state.getStatusCode().equals(UserStatusCode.ACTIVE) && state.getEntity().getRole() != UserRole.ADMIN) {
            throw new IllegalStateException("User is not valid");
        }

    }

    public UserState getLastUserState(@NonNull Long id, @NonNull User admin) {
        checkAdmin(admin);
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User not found"));
        return userStateRepository.findAllByEntity(user).stream().sorted().toList().getLast();
    }

    @Transactional
    public UserState updateUser(@NonNull Long id, @NonNull UserStatusCode userStatusCode, @NonNull User admin) {
        checkAdmin(admin);
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User not found"));
        UserState oldState = userStateRepository.findAllByEntity(user).stream().sorted().toList().getLast();
        if (oldState.getStatusCode().equals(userStatusCode)) {
            throw new IllegalStateException("User is already in that state");
        }
        if (userStatusCode == UserStatusCode.BANNED || userStatusCode == UserStatusCode.INACTIVE) {
            tokenController.logout(user);
        }
        return userStateRepository.save(new UserState(userStatusCode, admin, "User state changed", user, oldState));
    }

    public List<User> getAllUsers(@NonNull UserStatusCode status, @NonNull User admin) {
        return getAllUsers(admin).stream()
                .filter(user -> user.getRole() != UserRole.GUEST &&
                        user.getRole() != UserRole.TIME &&
                        getLastUserState(user.getId(), admin).getStatusCode().equals(status))
                .toList();
    }
}
