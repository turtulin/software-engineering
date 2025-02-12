package it.unicam.cs.ids2425.eshop.controller;

import it.unicam.cs.ids2425.articles.controller.ArticleController;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.model.reviews.Review;
import it.unicam.cs.ids2425.users.controller.actors.CustomerController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import it.unicam.cs.ids2425.utilities.wrappers.TypeToken;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
public class ReviewController implements IController {
    private final SingletonRepository<List<Review>, Review, Review> reviewRepository = SingletonRepository.getInstance(new TypeToken<>() {});

    private final CustomerController customerController = SingletonController.getInstance(new CustomerController() {});
    private final ArticleController articleController = SingletonController.getInstance(new ArticleController() {});

    public Review create(@NonNull IArticle article, @NonNull Review review, @NonNull IUser user) {
        article = articleController.get(article, ArticleStatus.PUBLISHED);
        user = customerController.get(user, UserStatus.ACTIVE);
        if (!review.article().equals(article) || !review.user().equals(user)) {
            review = new Review(user, article, review.rating(), review.title(), review.comment());
        }
        reviewRepository.create(review);
        return reviewRepository.get(review);
    }
}
