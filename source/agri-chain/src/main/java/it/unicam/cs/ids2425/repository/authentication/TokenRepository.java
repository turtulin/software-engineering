package it.unicam.cs.ids2425.repository.authentication;

import it.unicam.cs.ids2425.model.authentication.Token;
import it.unicam.cs.ids2425.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    void deleteTokensByUser(User user);
}
