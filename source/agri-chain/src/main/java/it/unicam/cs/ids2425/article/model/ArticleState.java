package it.unicam.cs.ids2425.article.model;

import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.utilities.state.AbstractState;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleState extends AbstractState {
    @ManyToOne(fetch = FetchType.EAGER)
    private Article entity;

    @OneToOne(fetch = FetchType.EAGER)
    private ArticleState oldState;

    public ArticleState(ArticleStatusCode articleStatusCode, User initiator, String reason, Article entity, ArticleState oldState) {
        super(articleStatusCode, initiator, reason);
        this.entity = entity;
        this.oldState = oldState;
    }
}
