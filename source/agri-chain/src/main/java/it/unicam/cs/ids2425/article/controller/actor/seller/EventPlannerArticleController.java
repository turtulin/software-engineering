package it.unicam.cs.ids2425.article.controller.actor.seller;

import it.unicam.cs.ids2425.article.controller.actor.SellerArticleController;
import it.unicam.cs.ids2425.article.model.ArticleType;
import it.unicam.cs.ids2425.article.model.article.Event;
import it.unicam.cs.ids2425.article.repository.ArticleStateRepository;
import it.unicam.cs.ids2425.article.repository.article.EventRepository;
import it.unicam.cs.ids2425.eshop.controller.stock.StockController;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventPlannerArticleController extends SellerArticleController<Event> {

    @Autowired
    public EventPlannerArticleController(ArticleStateRepository articleStatusRepository, EventRepository articleRepository, StockController stockController) {
        super(articleStatusRepository, articleRepository, stockController);
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
