package it.unicam.cs.ids2425.authentication.model;


import it.unicam.cs.ids2425.core.identifiers.Identifiable;
import it.unicam.cs.ids2425.users.model.IUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@EqualsAndHashCode
public class Token implements Identifiable<String> {
    private final Timestamp issueTime;
    private final IUser user;
    private final String token;

    public Token(IUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        this.user = user;
        issueTime = new Timestamp(System.currentTimeMillis());
        token = issueTime + user.getUsername();
    }

    @Override
    public String getId() {
        return token;
    }
}
