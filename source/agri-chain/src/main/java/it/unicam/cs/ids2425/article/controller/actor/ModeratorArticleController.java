package it.unicam.cs.ids2425.article.controller.actor;

import it.unicam.cs.ids2425.article.controller.AbstractArticleController;
import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.repository.ArticleRepository;
import it.unicam.cs.ids2425.article.repository.ArticleStateRepository;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeratorArticleController extends AbstractArticleController {
    public ModeratorArticleController(ArticleStateRepository articleStatusRepository, ArticleRepository articleRepository) {
        super(articleStatusRepository, articleRepository);
    }

    public List<Article> getAllArticles(@NonNull ArticleStatusCode articleStatusCode, User user) {
        if (user.getRole() != UserRole.MODERATOR) {
            throw new IllegalArgumentException("User must be a moderator");
        }
        return getAllArticles(articleStatusCode);
    }

}
