package it.unicam.cs.ids2425.eshop.controller.stock;

import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.eshop.model.stock.Stock;
import it.unicam.cs.ids2425.eshop.model.stock.StockContent;
import it.unicam.cs.ids2425.eshop.repository.stock.StockContentRepository;
import it.unicam.cs.ids2425.eshop.repository.stock.StockRepository;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StockController {
    private final StockRepository stockRepository;
    private final StockContentRepository stockContentRepository;

    @Autowired
    public StockController(StockRepository stockRepository, StockContentRepository stockContentRepository) {
        this.stockRepository = stockRepository;
        this.stockContentRepository = stockContentRepository;
    }

    public Stock findByUser(@NonNull User user) {
        return stockRepository.getStockByUser(user).orElseThrow(() -> new NoSuchElementException("User Stock not found"));
    }

    @Transactional
    public StockContent changeQuantity(@NonNull Stock stock, @NonNull Article article, @NonNull Long quantity) {
        if (quantity == 0) {
            throw new IllegalArgumentException("Quantity must not be 0");
        }

        StockContent stockContent = new StockContent(article, quantity);

        List<StockContent> articles = stock.getArticles();
        if (articles.contains(stockContent)) {
            StockContent sc = articles.get(articles.indexOf(stockContent));
            sc.setQuantity(sc.getQuantity() + stockContent.getQuantity());
            if (sc.getQuantity() <= 0) {
                articles.remove(sc);
                stockContentRepository.delete(sc);
            } else {
                stockContentRepository.save(sc);
            }
        } else {
            if (stockContent.getQuantity() < 0) {
                throw new IllegalArgumentException(STR."Can't remove \{article.getName()}: not present");
            }
            articles.add(stockContent);
            stockContentRepository.save(stockContent);
        }
        stockContentRepository.saveAll(articles);
        stockRepository.save(stock);
        return stockContent;
    }

    @Transactional
    public void createStock(@NonNull User user) {
        Stock stock = new Stock(user);
        stockRepository.save(stock);
    }

    @Transactional
    public void order(Stock stock) {
        for (StockContent stockContent : stock.getArticles()) {
            User stockContentSeller = stockContent.getArticle().getSeller();
            if (stockContentSeller.getRole().equals(UserRole.CUSTOMER)) {
                throw new NoSuchElementException("Seller not found");
            }
            Stock sellerStock;
            try {
                sellerStock = findByUser(stockContentSeller);
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException("Seller Stock not found");
            }

            if (!sellerStock.getArticles().contains(stockContent)) {
                throw new IllegalArgumentException("Article is not present in stock");
            }
            List<StockContent> articles = sellerStock.getArticles();
            StockContent actualStock = articles.get(articles.indexOf(stockContent));
            actualStock.reduceQuantity(stockContent.getQuantity());
            actualStock = stockContentRepository.save(actualStock);
            if (articles.get(articles.indexOf(actualStock)).getQuantity() == 0) {
                articles.remove(actualStock);
                stockContentRepository.delete(actualStock);
                stockContentRepository.saveAll(articles);
            } else {
                stockContentRepository.save(actualStock);
            }
            stockRepository.save(sellerStock);
        }
        stockContentRepository.saveAll(stock.getArticles());
    }
}
