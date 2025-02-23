package it.unicam.cs.ids2425.problem.model;

import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.utilities.state.AbstractState;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ProblemStatusCode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemState extends AbstractState {
    @ManyToOne
    private Problem entity;

    @OneToOne(fetch = FetchType.LAZY)
    private ProblemState oldState;

    public ProblemState(ProblemStatusCode articleStatusCode, User initiator, String reason, Problem entity, ProblemState oldState) {
        super(articleStatusCode, initiator, reason);
        this.entity = entity;
        this.oldState = oldState;
    }
}
