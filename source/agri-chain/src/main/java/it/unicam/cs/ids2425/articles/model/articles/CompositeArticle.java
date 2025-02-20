package it.unicam.cs.ids2425.articles.model.articles;

import it.unicam.cs.ids2425.users.model.IUser;

import java.util.ArrayList;
import java.util.List;

public sealed abstract class CompositeArticle extends GenericArticle permits ProcessedProduct, Package {
    private final List<IArticle> children = new ArrayList<>();

    public CompositeArticle(String name, String description, IUser sellerId, double price) {
        super(name, description, sellerId, price);
    }

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
