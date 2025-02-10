package it.unicam.cs.ids2425.problems.model;

import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.statuses.BaseStatus;

import java.sql.Timestamp;

public abstract class GenericProblem implements IProblem {
    private BaseStatus status;
    private String description;
    private IUser user;
    private final Timestamp creationDate = new Timestamp(System.currentTimeMillis());
}
