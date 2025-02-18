package it.unicam.cs.ids2425.users.model;

import it.unicam.cs.ids2425.utilities.statuses.State;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;

import java.sql.Timestamp;

public class UserState extends State<IUser, UserStatus, Long> {

    public UserState(IUser entity, UserStatus status, IUser initiator, String reason, State<IUser, UserStatus, Long> oldState, Timestamp stateTime) {
        super(entity, status, initiator, reason, oldState, stateTime);
    }

    public UserState(IUser entity, UserStatus status, IUser initiator, String reason, State<IUser, UserStatus, Long> oldState) {
        super(entity, status, initiator, reason, oldState);
    }

    public UserState(IUser entity, UserStatus status, IUser initiator) {
        super(entity, status, initiator);
    }

    public UserState(IUser entity, UserStatus status, IUser initiator, String reason) {
        super(entity, status, initiator, reason);
    }

    public UserState(IUser entity, UserStatus status, IUser initiator, State<IUser, UserStatus, Long> oldState) {
        super(entity, status, initiator, oldState);
    }
}
