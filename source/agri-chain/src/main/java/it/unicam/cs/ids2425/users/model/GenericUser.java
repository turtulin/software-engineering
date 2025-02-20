package it.unicam.cs.ids2425.users.model;

import it.unicam.cs.ids2425.core.identifiers.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Timestamp;

@AllArgsConstructor
@Data
@EqualsAndHashCode(of = {"username"})
@ToString
public abstract class GenericUser implements IUser, Identifiable<String> {
    private String username;
    private String password;
    private Timestamp birthDate;

    public GenericUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected GenericUser() {
    }

    @Override
    public String getId() {
        return username;
    }

    @Override
    public UserRole getRole() {
        return UserRole.GUEST;
    }
}
