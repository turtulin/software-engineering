package it.unicam.cs.ids2425.eshop.model.stocks;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.core.identifiers.Identifiable;
import it.unicam.cs.ids2425.users.model.IStockUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode(of = "user")
public abstract class GenericStock<T extends IStockUser, S extends IArticle> implements Identifiable<String>, Cloneable {
    private final Map<S, Integer> articles;
    private T user;

    public GenericStock(T user) {
        this.articles = new HashMap<>();
        this.user = user;
    }

    public void addArticle(@NonNull S article, int quantity) {
        if (articles.containsKey(article)) {
            articles.put(article, articles.get(article) + quantity);
        } else {
            articles.put(article, quantity);
        }
    }

    public boolean removeArticle(@NonNull S article, int quantity) {
        if (articles.containsKey(article)) {
            if (articles.get(article) == quantity) {
                articles.remove(article);
            } else if (articles.get(article) > quantity) {
                articles.put(article, articles.get(article) - quantity);
            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    public void empty() {
        articles.clear();
    }

    @Override
    public String getId() {
        return user.getId();
    }

    @Override
    public GenericStock<T, S> clone() {
        try {
            GenericStock<T, S> clone = user.getStock().getClass().getConstructor(user.getClass()).newInstance(user);
            clone.articles.clear();
            clone.articles.putAll(this.articles);
            clone.user = this.user;
            return clone;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
