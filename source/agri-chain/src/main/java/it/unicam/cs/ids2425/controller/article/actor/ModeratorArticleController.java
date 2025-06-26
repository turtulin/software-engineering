package it.unicam.cs.ids2425.controller.article.actor;

import it.unicam.cs.ids2425.controller.article.AbstractArticleController;
import it.unicam.cs.ids2425.model.article.Article;
import it.unicam.cs.ids2425.repository.article.AnyArticleRepository;
import it.unicam.cs.ids2425.repository.article.ArticleStateRepository;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.UserRole;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeratorArticleController extends AbstractArticleController<Article> {
    public ModeratorArticleController(ArticleStateRepository articleStatusRepository, AnyArticleRepository articleRepository) {
        super(articleStatusRepository, articleRepository);
    }

    public List<Article> getAllArticles(@NonNull ArticleStatusCode articleStatusCode, User user) {
        if (user.getRole() != UserRole.MODERATOR) {
            throw new IllegalArgumentException("User must be a moderator");
        }
        return getAllArticles(articleStatusCode);
    }

}
