package it.unicam.cs.ids2425.article.model;

import it.unicam.cs.ids2425.article.model.article.compositearticle.ComposableArticle;
import java.util.List;

public interface HasComponent {
    List<ComposableArticle> getComponents();
    void setComponents(List<ComposableArticle> components);
}
