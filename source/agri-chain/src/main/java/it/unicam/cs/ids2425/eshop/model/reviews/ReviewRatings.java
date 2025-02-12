package it.unicam.cs.ids2425.eshop.model.reviews;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReviewRatings {
    VERY_BAD(1),
    BAD(2),
    OK(3),
    GOOD(4),
    VERY_GOOD(5);

    private final int rating;
}
