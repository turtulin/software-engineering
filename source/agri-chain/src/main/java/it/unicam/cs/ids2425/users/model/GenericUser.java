package it.unicam.cs.ids2425.users.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class GenericUser implements IUser {
    private final Timestamp registrationDate = new Timestamp(System.currentTimeMillis());
    private String username;
    private String password;
    private Timestamp birthDate;

    @Override
    public UserRole getRole() {
        return UserRole.GUEST;
    }
}
