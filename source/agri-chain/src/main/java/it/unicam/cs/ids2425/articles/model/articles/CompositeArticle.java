package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.articles.model.GenericArticle;
import it.unicam.cs.ids2425.articles.model.IArticle;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public abstract class CompositeArticle extends GenericArticle {
    private final List<IArticle> children = new ArrayList<>();

    public void add(IArticle article) {
        if (canContain(article)) {
            children.add(article);
        } else {
            throw new IllegalArgumentException("Invalid child type for " + getClass().getSimpleName());
        }
    }

    public void remove(IArticle article) {
        children.remove(article);
    }

    public List<IArticle> getChildren() {
        return new ArrayList<>(children);
    }

    protected abstract boolean canContain(IArticle article);
}
