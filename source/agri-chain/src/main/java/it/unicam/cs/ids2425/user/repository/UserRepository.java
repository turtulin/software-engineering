package it.unicam.cs.ids2425.user.repository;

import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findByRole(UserRole userRole);

    List<User> findAllByUsername(String username);
}
