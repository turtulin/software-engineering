package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.articles.model.IArticle;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.statuses.BaseStatus;

public interface ISeller extends IUser {
    IArticle createArticleDraft(IArticle article);
    IArticle updateArticle(IArticle article);
    default boolean deleteArticle(IArticle article) {
        article.setStatus(BaseStatus.DELETED);
        return updateArticle(article) != null;
    }
    default IArticle publishArticle(IArticle article) {
        article.setStatus(BaseStatus.PENDING);
        return updateArticle(article);
    }
    default IArticle dismissArticle(IArticle article) {
        article.setStatus(BaseStatus.INACTIVE);
        return updateArticle(article);
    }
}
