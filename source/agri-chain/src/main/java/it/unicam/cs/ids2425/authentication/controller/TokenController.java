package it.unicam.cs.ids2425.authentication.controller;

import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
public class TokenController implements IController {
    private final SingletonRepository<Token> tokenRepository = SingletonRepository.getInstance(Token.class);

    public Token create(IUser user) {
        Token token = new Token(user);
        tokenRepository.save(token);
        return token;
    }

    // TODO: create an interceptor and use check in it,
    //  to get the token from the request and change it with the user making the request
    public IUser check(String token) {
        // Note: token lasts 2 days
        return tokenRepository.findAll().stream()
                .filter(t -> t.getToken().equals(token)
                        && t.getIssueTime()
                        .before(Timestamp.from(t.getIssueTime().toInstant().plusSeconds(60 * 60 * 24 * 2))))
                .map(Token::getUser).findFirst().orElse(new GenericUser("") {
                });
    }


    public void remove(Token token) {
        tokenRepository.deleteById(token);
    }
}
