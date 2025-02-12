package it.unicam.cs.ids2425.users.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "username")
public abstract class GenericUser implements IUser {
    private String username;
    private String password;
    private Timestamp birthDate;

    @Override
    public UserRole getRole() {
        return UserRole.GUEST;
    }
}
