package it.unicam.cs.ids2425.eshop.repository.review;

import it.unicam.cs.ids2425.eshop.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByArticle_Id(Long id);
}
