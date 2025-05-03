package it.unicam.cs.ids2425.article.controller.actor;

import it.unicam.cs.ids2425.article.controller.AbstractArticleController;
import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.repository.AnyArticleRepository;
import it.unicam.cs.ids2425.article.repository.ArticleStateRepository;
import it.unicam.cs.ids2425.eshop.controller.order.OrderController;
import it.unicam.cs.ids2425.eshop.controller.review.ReviewController;
import it.unicam.cs.ids2425.eshop.controller.stock.StockController;
import it.unicam.cs.ids2425.eshop.model.order.Order;
import it.unicam.cs.ids2425.eshop.model.review.Review;
import it.unicam.cs.ids2425.eshop.model.stock.Stock;
import it.unicam.cs.ids2425.eshop.model.stock.StockContent;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerArticleController extends AbstractArticleController<Article> {
    private final StockController stockController;
    private final OrderController orderController;
    private final ReviewController reviewController;

    @Autowired
    public CustomerArticleController(ArticleStateRepository articleStatusRepository, AnyArticleRepository articleRepository, StockController stockController, OrderController orderController, ReviewController reviewController) {
        super(articleStatusRepository, articleRepository);
        this.stockController = stockController;
        this.orderController = orderController;
        this.reviewController = reviewController;
    }

    @Override
    public List<Article> getAllArticles(@NonNull ArticleStatusCode articleStatusCode, User user) {
        if (user.getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("User must be a customer");
        }
        return getAllArticles(articleStatusCode);
    }

    public Stock getCart(@NonNull User user) {
        return stockController.findByUser(user);
    }

    @Transactional
    public StockContent addToCart(@NonNull Long articleId, @NonNull Long quantity, @NonNull User user) {
        return changeCartQuantity(articleId, quantity, user);
    }

    @Transactional
    public StockContent removeFromCart(@NonNull Long articleId, @NonNull Long quantity, @NonNull User user) {
        return changeCartQuantity(articleId, -quantity, user);
    }

    @Transactional
    protected StockContent changeCartQuantity(@NonNull Long articleId, @NonNull Long quantity, @NonNull User user) {
        Article article = getArticleById(articleId, ArticleStatusCode.PUBLISHED);
        Stock stock = stockController.findByUser(user);
        return stockController.changeQuantity(stock, article, quantity);
    }

    public Order checkout(@NonNull Order order, @NonNull User user) {
        order.setTrackingNumber(null);
        return orderController.create(order, user);
    }

    public Order cancelOrder(@NonNull Long orderId, @NonNull User user) {
        Order order = orderController.findById(orderId);
        if (order.getStock().getUser().equals(user)) {
            return orderController.cancel(order, user);
        }
        throw new IllegalArgumentException("Order does not belong to user");
    }

    public Review review(@NonNull Review review, @NonNull User user) {
        return reviewController.create(review, user);
    }

    public List<Order> getOrders(User user) {
        return orderController.findByUser(user);
    }
}
