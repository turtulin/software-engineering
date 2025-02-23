package it.unicam.cs.ids2425.article.controller.actor.seller;

import it.unicam.cs.ids2425.article.controller.actor.SellerArticleController;
import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.model.ArticleType;
import it.unicam.cs.ids2425.article.repository.ArticleRepository;
import it.unicam.cs.ids2425.article.repository.ArticleStateRepository;
import it.unicam.cs.ids2425.eshop.controller.stock.StockController;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransformerArticleController extends SellerArticleController {
    @Autowired
    public TransformerArticleController(ArticleStateRepository articleStatusRepository, ArticleRepository articleRepository, StockController stockController) {
        super(articleStatusRepository, articleRepository, stockController);
    }

    @Override
    public boolean checkArticleType(Article article) {
        return article.getType() == ArticleType.PROCESSED_PRODUCT;
    }

    @Override
    protected void checkSeller(@NonNull User user) {
        if (user.getRole() != UserRole.TRANSFORMER) {
            throw new IllegalArgumentException("User must be a transformer");
        }
    }
}
