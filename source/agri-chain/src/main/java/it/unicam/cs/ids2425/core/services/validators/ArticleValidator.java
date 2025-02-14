package it.unicam.cs.ids2425.core.services.validators;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.core.exceptions.AuthorizationException;
import it.unicam.cs.ids2425.core.exceptions.InvalidStateException;
import it.unicam.cs.ids2425.core.exceptions.ValidationException;
import it.unicam.cs.ids2425.core.model.UserId;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;

public class ArticleValidator {
    public void validateNewArticle(IArticle article) {
        if (article.getName() == null || article.getName().isBlank()) {
            throw new ValidationException(article.getName(), "Article name required");
        }
    }

    public void validatePublish(IArticle article, UserId requesterId) {
        if (!article.getSellerId().equals(requesterId)) {
            throw new AuthorizationException("Only creator can publish");
        }
        // TODO: Implement this part
        /* if (!(article.getState().getStatus() == ArticleStatus.DRAFT)) {
            throw new InvalidStateException(article.getState(), "Only DRAFT articles can be published");
        }*/
    }

    public void validateApproval(IArticle article, UserId approverId) {
        if (!approverId.value().startsWith("MOD-")) {
            throw new AuthorizationException("Requires moderator role");
        }
        // TODO: Implement this part
        /*
        if (!(article.getState().getStatus() == ArticleStatus.PENDING)) {
            throw new InvalidStateException(article.getState(), "Only PENDING articles can be approved");
        }*/
    }
}
