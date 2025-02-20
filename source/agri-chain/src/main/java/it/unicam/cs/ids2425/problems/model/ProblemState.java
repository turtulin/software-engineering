package it.unicam.cs.ids2425.problems.model;

import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.statuses.ProblemStatus;
import it.unicam.cs.ids2425.utilities.statuses.State;

import java.sql.Timestamp;

public class ProblemState extends State<IProblem, ProblemStatus, Long> {
    public ProblemState(IProblem entity, ProblemStatus status, IUser initiator, String reason, State<IProblem, ProblemStatus, Long> oldState, Timestamp stateTime) {
        super(entity, status, initiator, reason, oldState, stateTime);
    }

    public ProblemState(IProblem entity, ProblemStatus status, IUser initiator, String reason, State<IProblem, ProblemStatus, Long> oldState) {
        super(entity, status, initiator, reason, oldState);
    }

    public ProblemState(IProblem entity, ProblemStatus status, IUser initiator) {
        super(entity, status, initiator);
    }

    public ProblemState(IProblem entity, ProblemStatus status, IUser initiator, String reason) {
        super(entity, status, initiator, reason);
    }

    public ProblemState(IProblem entity, ProblemStatus status, IUser initiator, State<IProblem, ProblemStatus, Long> oldState) {
        super(entity, status, initiator, oldState);
    }
}
