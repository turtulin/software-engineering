package it.unicam.cs.ids2425.model.article;

import it.unicam.cs.ids2425.model.article.article.compositearticle.ComposableArticle;
import java.util.List;

public interface HasComponent {
    List<ComposableArticle> getComponents();
    void setComponents(List<ComposableArticle> components);
}
