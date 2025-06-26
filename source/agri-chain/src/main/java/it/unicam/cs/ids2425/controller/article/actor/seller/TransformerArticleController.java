package it.unicam.cs.ids2425.controller.article.actor.seller;

import it.unicam.cs.ids2425.controller.article.actor.SellerArticleController;
import it.unicam.cs.ids2425.model.article.ArticleType;
import it.unicam.cs.ids2425.model.article.article.compositearticle.ProcessedProduct;
import it.unicam.cs.ids2425.repository.article.AnyArticleRepository;
import it.unicam.cs.ids2425.repository.article.ArticleStateRepository;
import it.unicam.cs.ids2425.repository.article.article.compositearticle.ProcessedProductRepository;
import it.unicam.cs.ids2425.controller.eshop.stock.StockController;
import it.unicam.cs.ids2425.controller.user.SingleEntityController;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.UserRole;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransformerArticleController extends SellerArticleController<ProcessedProduct> {

    @Autowired
    public TransformerArticleController(ArticleStateRepository articleStatusRepository, ProcessedProductRepository articleRepository, StockController stockController, SingleEntityController singleEntityController, AnyArticleRepository anyArticleRepository) {
        super(articleStatusRepository, articleRepository, stockController, singleEntityController, anyArticleRepository);
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
