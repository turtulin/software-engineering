package it.unicam.cs.ids2425.utilities.statuses;

import it.unicam.cs.ids2425.core.identifiers.Identifiable;
import it.unicam.cs.ids2425.users.model.IUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@EqualsAndHashCode(of = "id")
public class State<T extends Identifiable<ID>, S extends IStatus, ID> implements Identifiable<ID>, Comparable<State<T, S, ID>> {
    private final ID id;
    private final T entity;
    private final S status;
    private final IUser initiator;
    private final State<T, S, ID> oldState;
    private final Timestamp stateTime;
    private final String reason;

    public State(T entity, S status, IUser initiator, String reason, State<T, S, ID> oldState, Timestamp stateTime) {
        this.id = entity.getId();
        this.entity = entity;
        this.status = status;
        this.initiator = initiator;
        this.oldState = oldState;
        this.stateTime = stateTime;
        this.reason = reason;
    }

    public State(T entity, S status, IUser initiator, String reason, State<T, S, ID> oldState) {
        this(entity, status, initiator, reason, oldState, new Timestamp(System.currentTimeMillis()));
    }

    public State(T entity, S status, IUser initiator) {
        this(entity, status, initiator, null, null);
    }

    public State(T entity, S status, IUser initiator, String reason) {
        this(entity, status, initiator, reason, null);
    }

    public State(T entity, S status, IUser initiator, State<T, S, ID> oldState) {
        this(entity, status, initiator, null, oldState);
    }

    @Override
    public int compareTo(State<T, S, ID> o) {
        return this.stateTime.compareTo(o.stateTime);
    }
}
