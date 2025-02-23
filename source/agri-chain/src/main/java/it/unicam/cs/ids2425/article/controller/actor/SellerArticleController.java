package it.unicam.cs.ids2425.article.controller.actor;

import it.unicam.cs.ids2425.article.controller.AbstractArticleController;
import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.model.ArticleState;
import it.unicam.cs.ids2425.article.repository.ArticleRepository;
import it.unicam.cs.ids2425.article.repository.ArticleStateRepository;
import it.unicam.cs.ids2425.eshop.controller.stock.StockController;
import it.unicam.cs.ids2425.eshop.model.stock.Stock;
import it.unicam.cs.ids2425.eshop.model.stock.StockContent;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import jakarta.transaction.Transactional;
import lombok.NonNull;

import java.util.List;

public abstract class SellerArticleController extends AbstractArticleController {
    private final StockController stockController;

    public SellerArticleController(ArticleStateRepository articleStatusRepository, ArticleRepository articleRepository, StockController stockController) {
        super(articleStatusRepository, articleRepository);
        this.stockController = stockController;
    }

    public List<Article> getAllArticles(@NonNull ArticleStatusCode articleStatusCode, @NonNull User user) {
        return getAllArticles(user).stream()
                .map(article -> articleStateRepository.findAllByEntity(article).getLast())
                .filter(articleState -> articleState.getStatusCode().equals(articleStatusCode))
                .map(ArticleState::getEntity)
                .toList();
    }

    public List<Article> getAllArticles(@NonNull User user) {
        checkSeller(user);
        return articleRepository.findAllBySeller(user).stream()
                .map(article -> articleStateRepository.findAllByEntity(article).getLast())
                .filter(articleState -> !articleState.getStatusCode().equals(ArticleStatusCode.DELETED))
                .map(ArticleState::getEntity)
                .toList();
    }

    public Article getArticle(@NonNull Long articleId, @NonNull ArticleStatusCode status, @NonNull User user) {
        checkSeller(user);
        ArticleState articleState = articleStateRepository.findAllByEntity_Id(articleId).getLast();
        if (articleState.getStatusCode().equals(status) && articleState.getEntity().getSeller().equals(user)) {
            return articleState.getEntity();
        }
        throw new IllegalArgumentException("Article not found");
    }

    public ArticleState updateArticle(@NonNull Long articleId, @NonNull ArticleStatusCode articleStatusCode, @NonNull User user) {
        checkSeller(user);
        ArticleState oldArticleState = articleStateRepository.findAllByEntity_Id(articleId).getLast();
        if (!oldArticleState.getEntity().getSeller().equals(user)) {
            throw new IllegalArgumentException("Article not found");
        }
        ArticleState newArticleState = new ArticleState(articleStatusCode, user, null, oldArticleState.getEntity(), oldArticleState);
        articleStateRepository.save(newArticleState);
        return articleStateRepository.findAllByEntity_Id(articleId).getLast();
    }

    protected abstract void checkSeller(@NonNull User user);

    protected abstract boolean checkArticleType(Article article);

    @Transactional
    public Article createArticle(@NonNull Article article, @NonNull User user) {
        checkSeller(user);

        if (articleRepository.findByName(article.getName()).isPresent()) {
            throw new IllegalArgumentException("Article already exists");
        }

        article.setSeller(user);

        if (article.getType() == null || !checkArticleType(article)) {
            throw new IllegalArgumentException("Article type must be specified");
        }

        ArticleState state = new ArticleState(ArticleStatusCode.DRAFT, user, null, article, null);
        articleRepository.save(article);
        articleStateRepository.save(state);

        return getArticleById(article.getId(), ArticleStatusCode.DRAFT);
    }

    @Transactional
    public Article updateArticle(@NonNull Article article, @NonNull User user) {
        checkSeller(user);

        if (articleRepository.findById(article.getId()).isEmpty()) {
            throw new IllegalArgumentException("Article not found");
        }

        User seller = articleRepository.findById(article.getId()).get().getSeller();

        if (article.getSeller() == null || !article.getSeller().equals(user) || !seller.equals(user)) {
            throw new IllegalArgumentException("Article seller must be the same as the user");
        }

        if (article.getType() == null || !checkArticleType(article)) {
            throw new IllegalArgumentException("Article type must be specified");
        }

        article.setSeller(seller);

        ArticleState oldState = articleStateRepository.findAllByEntity(article).getLast();
        ArticleState state = new ArticleState(ArticleStatusCode.DRAFT, user, "updated", article, oldState);

        articleRepository.save(article);
        articleStateRepository.save(state);

        return getArticleById(article.getId(), ArticleStatusCode.DRAFT);
    }

    public Article updateArticleQuantity(@NonNull Long articleId, @NonNull Long quantity, @NonNull User user) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        return changeStockQuantity(articleId, quantity, user).getArticle();
    }

    public Long getArticleQuantity(@NonNull Long articleId, @NonNull User user) {
        List<StockContent> stockContents = stockController.findByUser(user).getArticles();
        return stockContents.get(
                stockContents.indexOf(new StockContent(
                        articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("Article not found")),
                        0L))).getQuantity();
    }

    private StockContent changeStockQuantity(@NonNull Long articleId, @NonNull Long quantity, @NonNull User user) {
        Article article = getArticleById(articleId, ArticleStatusCode.PUBLISHED);
        Stock stock = stockController.findByUser(user);
        return stockController.changeQuantity(stock, article, quantity);
    }
}
