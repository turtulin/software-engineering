package it.unicam.cs.ids2425.model.user;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@Entity
@ToString
@EqualsAndHashCode(of = "username")
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UserRole role;

    private String username;

    private String password;

    private Timestamp birthDate;

    public User(UserRole role, String username, String password, Timestamp birthDate) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
    }
}
