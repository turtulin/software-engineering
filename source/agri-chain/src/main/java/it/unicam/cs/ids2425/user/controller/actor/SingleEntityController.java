package it.unicam.cs.ids2425.user.controller.actor;

import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import it.unicam.cs.ids2425.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class SingleEntityController {
    private final UserRepository userRepository;

    public SingleEntityController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getTimeUser() {
        User time = new User(UserRole.TIME, "time", null, new Timestamp(System.currentTimeMillis()));
        return userRepository.findByRole(UserRole.TIME)
                .orElseGet(() -> userRepository.save(time));
    }

    public User getGuestUser() {
        User guest = new User(UserRole.GUEST, "guest", null, new Timestamp(System.currentTimeMillis()));
        return userRepository.findByRole(UserRole.GUEST)
                .orElseGet(() -> userRepository.save(guest));
    }
}
