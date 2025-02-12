package it.unicam.cs.ids2425.problems.model;

import it.unicam.cs.ids2425.users.model.IUser;
import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public abstract class GenericProblem<T> implements IProblem {
    private final String description;
    private final T problem;
    @Setter
    private IUser user;
}
