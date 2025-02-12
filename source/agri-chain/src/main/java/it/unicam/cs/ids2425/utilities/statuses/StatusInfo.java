package it.unicam.cs.ids2425.utilities.statuses;

import it.unicam.cs.ids2425.users.model.IUser;

import java.sql.Timestamp;
import java.util.Comparator;

public record StatusInfo<T extends IStatus>(T status, IUser user, String message, Timestamp timestamp) implements Comparable<StatusInfo<T>>, Comparator<StatusInfo<T>> {
    public StatusInfo(T status, IUser user, String message) {
        this(status, user, message, new Timestamp(System.currentTimeMillis()));
    }

    public StatusInfo(T status, IUser user) {
        this(status, user, null, new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public int compareTo(StatusInfo<T> o) {
        return this.timestamp.compareTo(o.timestamp);
    }

    @Override
    public int compare(StatusInfo<T> o1, StatusInfo<T> o2) {
        return o1.compareTo(o2);
    }
}
