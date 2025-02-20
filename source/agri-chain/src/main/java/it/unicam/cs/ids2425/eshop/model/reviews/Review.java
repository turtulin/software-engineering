package it.unicam.cs.ids2425.eshop.model.reviews;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.core.identifiers.Identifiable;
import it.unicam.cs.ids2425.users.model.IUser;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

@Getter
public class Review implements Identifiable<Long> {
    private static Long lastId = 0L;
    private final Long id;
    private final IUser user;
    private final IArticle article;
    private final ReviewRatings rating;
    private final String title;
    private final String comment;

    public Review(@NonNull IUser user, @NonNull IArticle article, @NonNull ReviewRatings rating, @NonNull String title,
                  String comment) {
        this.id = ++lastId;
        this.user = user;
        this.article = article;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(user, review.user) && Objects.equals(article, review.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, article);
    }
}
