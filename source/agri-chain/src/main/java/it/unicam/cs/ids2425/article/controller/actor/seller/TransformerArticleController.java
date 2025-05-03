package it.unicam.cs.ids2425.article.controller.actor.seller;

import it.unicam.cs.ids2425.article.controller.actor.SellerArticleController;
import it.unicam.cs.ids2425.article.model.ArticleType;
import it.unicam.cs.ids2425.article.model.article.compositearticle.ProcessedProduct;
import it.unicam.cs.ids2425.article.repository.ArticleStateRepository;
import it.unicam.cs.ids2425.article.repository.article.compositearticle.ProcessedProductRepository;
import it.unicam.cs.ids2425.eshop.controller.stock.StockController;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransformerArticleController extends SellerArticleController<ProcessedProduct> {

    @Autowired
    public TransformerArticleController(ArticleStateRepository articleStatusRepository, ProcessedProductRepository articleRepository, StockController stockController) {
        super(articleStatusRepository, articleRepository, stockController);
    }

    @Override
    protected void checkSeller(@NonNull User user) {
        if (user.getRole() != UserRole.TRANSFORMER) {
            throw new IllegalArgumentException("User must be a transformer");
        }
    }

    @Override
    public boolean notCorrectArticleType(ProcessedProduct article) {
        return article.getType() != ArticleType.PROCESSED_PRODUCT || !ArticleType.PROCESSED_PRODUCT.getEntityClass().equals(article.getClass());
    }
}
