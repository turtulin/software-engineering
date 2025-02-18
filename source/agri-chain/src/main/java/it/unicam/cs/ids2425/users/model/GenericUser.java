package it.unicam.cs.ids2425.users.model;

import it.unicam.cs.ids2425.core.identifiers.Identifiable;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@ToString
public abstract class GenericUser implements IUser, Identifiable<Long> {
    private static long currentLastId = 0L;
    private Long id;
    private String username;
    private String password;
    private Timestamp birthDate;

    public GenericUser(String username) {
        this.username = username;
        id = ++currentLastId;
    }

    @Override
    public UserRole getRole() {
        return UserRole.GUEST;
    }
}
