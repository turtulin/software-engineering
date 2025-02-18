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
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
public class ReviewController implements IController {
    private final SingletonRepository<Review> reviewRepository = SingletonRepository.getInstance(Review.class);

    private final CustomerController customerController = SingletonController.getInstance(new CustomerController() {
    });
    private final ArticleController articleController = SingletonController.getInstance(new ArticleController() {
    });

    public Review create(@NonNull IArticle article, @NonNull Review review, @NonNull IUser user) {
        article = articleController.get(article, ArticleStatus.PUBLISHED);
        user = customerController.get(user, UserStatus.ACTIVE);
        if (!review.getArticle().equals(article) || !review.getUser().equals(user)) {
            review = new Review(user, article, review.getRating(), review.getTitle(), review.getComment());
        }
        reviewRepository.save(review);
        return reviewRepository.findById(review).get();
    }
}
