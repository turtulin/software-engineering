package it.unicam.cs.ids2425.controller.article.actor;

import it.unicam.cs.ids2425.controller.article.AbstractArticleController;
import it.unicam.cs.ids2425.model.article.Article;
import it.unicam.cs.ids2425.repository.article.AnyArticleRepository;
import it.unicam.cs.ids2425.repository.article.ArticleStateRepository;
import it.unicam.cs.ids2425.controller.eshop.order.OrderController;
import it.unicam.cs.ids2425.controller.eshop.review.ReviewController;
import it.unicam.cs.ids2425.controller.eshop.stock.StockController;
import it.unicam.cs.ids2425.model.eshop.order.Order;
import it.unicam.cs.ids2425.model.eshop.order.OrderState;
import it.unicam.cs.ids2425.model.eshop.review.Review;
import it.unicam.cs.ids2425.model.eshop.stock.Stock;
import it.unicam.cs.ids2425.model.eshop.stock.StockContent;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.UserRole;
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

    public OrderState getOrderStatus(Long orderId, User user) {
        return orderController.getOrderStatus(orderId, user);
    }
}
