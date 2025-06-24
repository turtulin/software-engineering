package it.unicam.cs.ids2425.controller.article.actor.seller;

import it.unicam.cs.ids2425.controller.article.actor.SellerArticleController;
import it.unicam.cs.ids2425.model.article.ArticleType;
import it.unicam.cs.ids2425.model.article.article.Event;
import it.unicam.cs.ids2425.repository.article.AnyArticleRepository;
import it.unicam.cs.ids2425.repository.article.ArticleStateRepository;
import it.unicam.cs.ids2425.repository.article.article.EventRepository;
import it.unicam.cs.ids2425.controller.eshop.stock.StockController;
import it.unicam.cs.ids2425.controller.user.SingleEntityController;
import it.unicam.cs.ids2425.model.user.User;
import it.unicam.cs.ids2425.model.user.UserRole;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventPlannerArticleController extends SellerArticleController<Event> {

    @Autowired
    public EventPlannerArticleController(ArticleStateRepository articleStatusRepository, EventRepository articleRepository, StockController stockController, SingleEntityController singleEntityController, AnyArticleRepository anyArticleRepository) {
        super(articleStatusRepository, articleRepository, stockController, singleEntityController, anyArticleRepository);
    }

    @Override
    protected void checkSeller(@NonNull User user) {
        if (user.getRole() != UserRole.EVENT_PLANNER) {
            throw new IllegalArgumentException("User must be a EventPlanner");
        }
    }

    @Override
    public boolean notCorrectArticleType(Event article) {
        return article.getType() != ArticleType.EVENT || !ArticleType.EVENT.getEntityClass().equals(article.getClass());
    }
}
