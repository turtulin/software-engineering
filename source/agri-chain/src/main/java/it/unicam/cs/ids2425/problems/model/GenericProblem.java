package it.unicam.cs.ids2425.problems.model;

import it.unicam.cs.ids2425.users.model.IUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = "id")
public abstract class GenericProblem<T> implements IProblem {
    private static long lastId = 0;
    private final Long id;
    private final String description;
    private final T problem;
    @Setter
    private IUser user;

    public GenericProblem(String description, T problem, IUser user) {
        this.id = ++lastId;
        this.description = description;
        this.problem = problem;
        this.user = user;
    }
}
