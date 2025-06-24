package it.unicam.cs.ids2425.utilities.state;

import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.utilities.statuscode.BaseStatusCode;
import it.unicam.cs.ids2425.utilities.statuscode.IStatusCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractState implements Comparable<AbstractState> {
    @Id
    @GeneratedValue
    private Long id;
    private BaseStatusCode statusCode;
    @ManyToOne(fetch = FetchType.EAGER)
    private User initiator;
    private Timestamp stateTime;
    private String reason;

    public AbstractState(IStatusCode statusCode, User initiator, String reason) {
        this.statusCode = BaseStatusCode.fromCode(statusCode);
        this.initiator = initiator;
        this.reason = reason;
        this.stateTime = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public int compareTo(AbstractState o) {
        return this.stateTime.compareTo(o.stateTime);
    }
}