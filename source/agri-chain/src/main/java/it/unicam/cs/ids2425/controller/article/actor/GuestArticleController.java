package it.unicam.cs.ids2425.controller.article.actor;

import it.unicam.cs.ids2425.controller.article.AbstractArticleController;
import it.unicam.cs.ids2425.model.article.Article;
import it.unicam.cs.ids2425.repository.article.AnyArticleRepository;
import it.unicam.cs.ids2425.repository.article.ArticleStateRepository;
import it.unicam.cs.ids2425.controller.eshop.review.ReviewController;
import it.unicam.cs.ids2425.model.eshop.review.Review;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.UserRole;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestArticleController extends AbstractArticleController<Article> {
    private final ReviewController reviewController;

    @Autowired
    public GuestArticleController(ArticleStateRepository articleStatusRepository, AnyArticleRepository articleRepository, ReviewController reviewController) {
        super(articleStatusRepository, articleRepository);
        this.reviewController = reviewController;
    }

    @Override
    public List<Article> getAllArticles(@NonNull ArticleStatusCode articleStatusCode, User user) {
        if (user.getRole() != UserRole.GUEST) {
            throw new IllegalArgumentException("Only guest can view all articles");
        }
        if (articleStatusCode != ArticleStatusCode.PUBLISHED) {
            throw new IllegalArgumentException("Guest can only view published articles");
        }
        return getAllArticles(ArticleStatusCode.PUBLISHED);
    }

    @Transactional
    public List<Review> getArticleReviewsById(Long id, ArticleStatusCode articleStatusCode) {
        Article article = getArticleById(id, articleStatusCode);
        return reviewController.findAllById(article.getId());
    }
}
