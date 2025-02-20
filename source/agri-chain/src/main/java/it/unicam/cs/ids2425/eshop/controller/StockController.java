package it.unicam.cs.ids2425.eshop.controller;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.model.stocks.GenericStock;
import it.unicam.cs.ids2425.users.model.IStockUser;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;

@NoArgsConstructor
public abstract class StockController<T extends GenericStock<S, Q>, S extends IStockUser, Q extends IArticle> implements IController {

    protected abstract SingletonRepository<T> getStockRepository();

    private T createStock(S user) {
        try {
            return (T) user.getStock().getClass().getConstructor(user.getClass()).newInstance(user);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("Constructor not found", e);
        }
    }

    private T getStock(@NonNull S user) {
        return getStockRepository().findById((T) user.getStock()).orElseThrow(() -> new NoSuchElementException("Cart not found"));
    }

    public T addArticleToStock(@NonNull Q article, @NonNull S user, int quantity) {
        T stock = (T) user.getStock();

        stock.addArticle(article, quantity);
        getStockRepository().save(stock);
        return getStock(stock.getUser());
    }

    public T removeArticleFromStock(@NonNull Q article, @NonNull S user, int quantity) {
        T stock = (T) user.getStock();

        if (!stock.removeArticle(article, quantity)) {
            throw new IllegalArgumentException("Article Problem");
        }
        getStockRepository().save(stock);
        return getStock(stock.getUser());
    }

    public T get(@NonNull T stock) {
        return getStock(stock.getUser());
    }

    public T create(@NonNull S user) {
        T stock = (T) user.getStock();

        if (stock == null) {
            user.setStock(createStock(user));
            stock = (T) user.getStock();
        }
        getStockRepository().save(stock);
        return getStock(stock.getUser());
    }

    public void empty(@NonNull T stock) {
        stock = getStock(stock.getUser());
        stock.empty();
        getStockRepository().save(stock);
    }

    public Integer getArticleQuantity(@NonNull Q article, @NonNull S user) {
        return getStock(user).getArticles().getOrDefault(article, 0);
    }
}
