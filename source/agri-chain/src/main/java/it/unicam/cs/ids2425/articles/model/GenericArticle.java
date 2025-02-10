package it.unicam.cs.ids2425.articles.model;

import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import it.unicam.cs.ids2425.utilities.statuses.BaseStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class GenericArticle implements IArticle {
    private String name;
    private String description;
    private String content;
    private int availableArticles;
    private double price;
    private BaseStatus status;
    @Setter(AccessLevel.NONE)
    private ISeller seller;
    @Setter(AccessLevel.NONE)
    private ArticleType type;
}
