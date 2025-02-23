package it.unicam.cs.ids2425.authentication.model;


import it.unicam.cs.ids2425.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(of = {"token"})
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Timestamp issueTime;

    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;

    private String token;

    public Token(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        this.user = user;
        issueTime = new Timestamp(System.currentTimeMillis());
        token = String.format("%015x", new BigInteger(1, (issueTime + user.getUsername()).getBytes()));
    }
}
