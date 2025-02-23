package it.unicam.cs.ids2425.authentication.controller;

import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.authentication.repository.TokenRepository;
import it.unicam.cs.ids2425.user.controller.actor.SingleEntityController;
import it.unicam.cs.ids2425.user.model.User;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class TokenController {
    private final TokenRepository tokenRepository;
    private final SingleEntityController singleEntityController;

    @Autowired
    protected TokenController(TokenRepository tokenRepository, SingleEntityController singleEntityController) {
        this.tokenRepository = tokenRepository;
        this.singleEntityController = singleEntityController;
    }

    public Token createToken(@NonNull User user) {
        Token token = new Token(user);
        tokenRepository.save(token);
        return token;
    }

    public User check(@NonNull String token) {
        // Note: token lasts 2 days
        return tokenRepository.findAll().stream()
                .filter(t -> t.getToken().equals(token)
                        && t.getIssueTime()
                        .before(Timestamp.from(t.getIssueTime().toInstant().plusSeconds(60 * 60 * 24 * 2))))
                .map(Token::getUser).findFirst().orElse(singleEntityController.getGuestUser());
    }


    @Transactional
    public void deleteToken(@NonNull User user) {
        tokenRepository.deleteTokensByUser(user);
    }

    public Token get(@NonNull String token) {
        return tokenRepository.findAll().stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Token not found"));
    }

    @Transactional
    public void logout(User user) {
        tokenRepository.deleteTokensByUser(user);
    }
}
