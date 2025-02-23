package it.unicam.cs.ids2425.user.repository;

import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStateRepository extends JpaRepository<UserState, Long> {
    List<UserState> findAllByEntity(User user);
}
