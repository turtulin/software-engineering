package it.unicam.cs.ids2425.users.model;

import it.unicam.cs.ids2425.articles.model.IArticle;
import it.unicam.cs.ids2425.problems.model.IProblem;
import it.unicam.cs.ids2425.utilities.statuses.BaseStatus;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class GenericUser implements IUser {
    private String username;
    private String password;
    private UserRole role = UserRole.GUEST;
    private Timestamp birthDate;
    private BaseStatus status;
    private final Timestamp registrationDate = new Timestamp(System.currentTimeMillis());

    @Override
    public List<IArticle> viewArticles() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shareContent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public IProblem reportProblem() {
        throw new UnsupportedOperationException();
    }
}
