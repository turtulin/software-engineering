package it.unicam.cs.ids2425.article.controller.actor.seller;

import it.unicam.cs.ids2425.article.controller.actor.SellerArticleController;
import it.unicam.cs.ids2425.article.model.ArticleType;
import it.unicam.cs.ids2425.article.model.article.compositearticle.RawMaterial;
import it.unicam.cs.ids2425.article.repository.AnyArticleRepository;
import it.unicam.cs.ids2425.article.repository.ArticleStateRepository;
import it.unicam.cs.ids2425.article.repository.article.compositearticle.RawMaterialRepository;
import it.unicam.cs.ids2425.eshop.controller.stock.StockController;
import it.unicam.cs.ids2425.user.controller.actor.SingleEntityController;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProducerArticleController extends SellerArticleController<RawMaterial> {

    @Autowired
    public ProducerArticleController(ArticleStateRepository articleStatusRepository, RawMaterialRepository articleRepository, StockController stockController, SingleEntityController singleEntityController, AnyArticleRepository anyArticleRepository) {
        super(articleStatusRepository, articleRepository, stockController, singleEntityController, anyArticleRepository);
    }

    @Override
    protected void checkSeller(@NonNull User user) {
        if (user.getRole() != UserRole.PRODUCER) {
            throw new IllegalArgumentException("User must be a producer");
        }
    }

    @Override
    public boolean notCorrectArticleType(RawMaterial article) {
        return article.getType() != ArticleType.RAW_MATERIAL || !ArticleType.RAW_MATERIAL.getEntityClass().equals(article.getClass());
    }
}
