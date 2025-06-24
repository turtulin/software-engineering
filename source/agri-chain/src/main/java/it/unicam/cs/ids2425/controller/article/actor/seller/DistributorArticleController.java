package it.unicam.cs.ids2425.controller.article.actor.seller;

import it.unicam.cs.ids2425.controller.article.actor.SellerArticleController;
import it.unicam.cs.ids2425.model.article.ArticleType;
import it.unicam.cs.ids2425.model.article.article.Package;
import it.unicam.cs.ids2425.repository.article.AnyArticleRepository;
import it.unicam.cs.ids2425.repository.article.ArticleStateRepository;
import it.unicam.cs.ids2425.repository.article.article.PackageRepository;
import it.unicam.cs.ids2425.controller.eshop.stock.StockController;
import it.unicam.cs.ids2425.controller.user.SingleEntityController;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.UserRole;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistributorArticleController extends SellerArticleController<Package> {

    @Autowired
    public DistributorArticleController(ArticleStateRepository articleStatusRepository, StockController stockController, PackageRepository articleRepository, SingleEntityController singleEntityController, AnyArticleRepository anyArticleRepository) {
        super(articleStatusRepository, articleRepository, stockController, singleEntityController, anyArticleRepository);
    }

    @Override
    protected void checkSeller(@NonNull User user) {
        if (user.getRole() != UserRole.DISTRIBUTOR) {
            throw new IllegalArgumentException("User must be a distributor");
        }
    }

    @Override
    public boolean notCorrectArticleType(Package article) {
        return article.getType() != ArticleType.PACKAGE || !ArticleType.PACKAGE.getEntityClass().equals(article.getClass());
    }
}
