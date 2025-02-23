package it.unicam.cs.ids2425.eshop.model.review;

import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.user.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @OneToOne
    private Article article;

    private ReviewRating rating;
    private String title;
    private String comment;
}
