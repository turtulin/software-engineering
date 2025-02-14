package it.unicam.cs.ids2425.authentication.controller;

import it.unicam.cs.ids2425.authentication.model.Token;
import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.wrappers.TypeToken;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
public class TokenController implements IController {
    private final SingletonRepository<List<Token>, Token, Token> tokenRepository = SingletonRepository.getInstance(new TypeToken<>() {});

    public Token create(IUser user) {
        Token token = new Token(user);
        tokenRepository.create(token);
        return token;
    }

    // TODO: create an interceptor and use check in it,
    //  to get the token from the request and change it with the user making the request
    public IUser check(String token) {
        // Note: token lasts 2 days
        return tokenRepository.getAll().stream()
                .filter(t -> t.getToken().equals(token)
                        && t.getIssueTime()
                            .before(Timestamp.from(t.getIssueTime().toInstant().plusSeconds(60 * 60 * 24 * 2))))
                .map(Token::getUser).findFirst().orElse(new GenericUser() {});
    }



    public void remove(Token token) {
        tokenRepository.remove(token);
    }
}
