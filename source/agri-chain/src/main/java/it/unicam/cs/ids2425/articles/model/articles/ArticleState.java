package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.statuses.State;

import java.sql.Timestamp;

public class ArticleState extends State<IArticle, ArticleStatus, Long> {
    public ArticleState(IArticle entity, ArticleStatus status, IUser initiator, String reason, State<IArticle, ArticleStatus, Long> oldState, Timestamp stateTime) {
        super(entity, status, initiator, reason, oldState, stateTime);
    }

    public ArticleState(IArticle entity, ArticleStatus status, IUser initiator, String reason, State<IArticle, ArticleStatus, Long> oldState) {
        super(entity, status, initiator, reason, oldState);
    }

    public ArticleState(IArticle entity, ArticleStatus status, IUser initiator) {
        super(entity, status, initiator);
    }

    public ArticleState(IArticle entity, ArticleStatus status, IUser initiator, String reason) {
        super(entity, status, initiator, reason);
    }

    public ArticleState(IArticle entity, ArticleStatus status, IUser initiator, State<IArticle, ArticleStatus, Long> oldState) {
        super(entity, status, initiator, oldState);
    }
}
