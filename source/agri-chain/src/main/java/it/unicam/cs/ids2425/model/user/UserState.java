package it.unicam.cs.ids2425.model.user;

import it.unicam.cs.ids2425.utilities.state.AbstractState;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.UserStatusCode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserState extends AbstractState {
    @ManyToOne
    private User entity;

    @OneToOne(fetch = FetchType.LAZY)
    private UserState oldState;

    public UserState(UserStatusCode userStatusCode, User initiator, String reason, User entity, UserState oldState) {
        super(userStatusCode, initiator, reason);
        this.entity = entity;
        this.oldState = oldState;
    }
}
