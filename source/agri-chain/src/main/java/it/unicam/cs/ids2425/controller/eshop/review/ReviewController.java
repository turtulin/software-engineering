package it.unicam.cs.ids2425.controller.eshop.review;

import it.unicam.cs.ids2425.model.eshop.review.Review;
import it.unicam.cs.ids2425.repository.eshop.review.ReviewRepository;
import it.unicam.cs.ids2425.model.user.User;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewController {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review create(@NonNull Review review, @NonNull User user) {
        review.setUser(user);
        return reviewRepository.save(review);
    }

    public List<Review> findAllById(Long id) {
        return reviewRepository.findAllByArticle_Id(id);
    }
}
