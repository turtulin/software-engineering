package it.unicam.cs.ids2425.user.controller.actor;

import it.unicam.cs.ids2425.authentication.controller.TokenController;
import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserState;
import it.unicam.cs.ids2425.user.repository.UserRepository;
import it.unicam.cs.ids2425.user.repository.UserStateRepository;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.UserStatusCode;
import jakarta.transaction.Transactional;
import lombok.NonNull;

import java.sql.Timestamp;
import java.util.NoSuchElementException;

public abstract class UserController {
    protected final UserRepository userRepository;
    protected final UserStateRepository userStateRepository;
    private final TokenController tokenController;
    private final SingleEntityController singleEntityController;

    public UserController(UserRepository userRepository, TokenController tokenController, UserStateRepository userStateRepository, SingleEntityController singleEntityController) {
        this.userRepository = userRepository;
        this.tokenController = tokenController;
        this.userStateRepository = userStateRepository;
        this.singleEntityController = singleEntityController;
    }

    @Transactional
    public Token login(@NonNull String username, @NonNull String password) {
        User u = userRepository.findUserByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found"));
        if (!u.getPassword().equals(password)) {
            throw new NoSuchElementException("Wrong Password");
        }
        UserState state = userStateRepository.findAllByEntity(u).stream().sorted().toList().getLast();
        if (state.getStatusCode().equals(UserStatusCode.ACTIVE)) {
            return tokenController.createToken(u);
        } else if (state.getStatusCode().equals(UserStatusCode.BANNED)) {
            throw new IllegalStateException("User is banned");
        } else if (state.getStatusCode().equals(UserStatusCode.PENDING)) {
            throw new IllegalStateException("User is pending");
        } else if (state.getStatusCode().equals(UserStatusCode.DELETED)) {
            throw new IllegalStateException("User is deleted");
        } else if (state.getStatusCode().equals(UserStatusCode.INACTIVE)) {
            return checkReenable(u);
        }
        throw new IllegalStateException("User is inactive");
    }

    @Transactional
    protected Token checkReenable(@NonNull User u) {
        UserState state = userStateRepository.findAllByEntity(u).stream().sorted().toList().getLast();
        if (state.getStateTime().before(new Timestamp(System.currentTimeMillis()))) {
            UserStatusCode nextState = UserStatusCode.fromCode(userStateRepository.findAllByEntity(u).stream().sorted().toList().get(userStateRepository.findAllByEntity(u).size() - 2).getStatusCode());
            UserState previousState = userStateRepository.findAllByEntity(u).stream().sorted().toList().getLast();
            UserState newState = new UserState(nextState, singleEntityController.getTimeUser(), "automatic suspension removal", u, previousState);
            userStateRepository.save(newState);
            return login(u.getUsername(), u.getPassword());
        }
        throw new IllegalStateException("User is inactive");
    }

    public abstract User register(@NonNull User user);

    public void logout(@NonNull User user) {
        tokenController.deleteToken(user);
    }
}
