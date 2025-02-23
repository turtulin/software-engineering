package it.unicam.cs.ids2425.authentication.repository;

import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    void deleteTokensByUser(User user);
}
