package it.unicam.cs.ids2425.eshop.model.reviews;

import it.unicam.cs.ids2425.articles.model.IArticle;
import it.unicam.cs.ids2425.users.model.IUser;
import lombok.NonNull;

import java.util.Objects;

public record Review(@NonNull IUser user, @NonNull IArticle article, ReviewRatings rating, @NonNull String title, String comment) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(user(), review.user()) && Objects.equals(article(), review.article());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user(), article());
    }
}
