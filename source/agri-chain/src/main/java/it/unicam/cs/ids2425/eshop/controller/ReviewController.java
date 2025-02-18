package it.unicam.cs.ids2425.eshop.controller;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.model.reviews.Review;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Optional;

@NoArgsConstructor
public class ReviewController implements IController {
    private final SingletonRepository<Review> reviewRepository = SingletonRepository.getInstance(Review.class);


    public Review create(@NonNull IArticle article, @NonNull Review review, @NonNull IUser user) {
        if (!review.getArticle().equals(article) || !review.getUser().equals(user)) {
            review = new Review(user, article, review.getRating(), review.getTitle(), review.getComment());
        }
        reviewRepository.save(review);
        Optional<Review> optionalReview = reviewRepository.findById(review);
        if (optionalReview.isPresent()) {
            return optionalReview.get();
        }
        throw new RuntimeException("Review not found");
    }
}
