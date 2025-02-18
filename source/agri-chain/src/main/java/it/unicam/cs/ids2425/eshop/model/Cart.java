package it.unicam.cs.ids2425.eshop.model;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.core.identifiers.Identifiable;
import it.unicam.cs.ids2425.users.model.actors.Customer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode(of = "user")
public class Cart implements Identifiable<String>, Cloneable {
    private final Map<IArticle, Integer> articles;
    private final Customer user;

    public Cart(Customer user) {
        this.articles = new HashMap<>();
        this.user = user;
    }

    public void addArticle(@NonNull IArticle article) {
        if (articles.containsKey(article)) {
            articles.put(article, articles.get(article) + 1);
        } else {
            articles.put(article, 1);
        }
    }

    public void removeArticle(@NonNull IArticle article) {
        if (articles.containsKey(article)) {
            if (articles.get(article) == 1) {
                articles.remove(article);
            } else {
                articles.put(article, articles.get(article) - 1);
            }
        }
    }

    public void empty() {
        articles.clear();
    }

    @Override
    public String getId() {
        return user.getId();
    }

    @Override
    public Cart clone() {
        Cart clone = new Cart(user);
        clone.articles.putAll(this.articles);
        return clone;
    }
}
