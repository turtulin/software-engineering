package it.unicam.cs.ids2425.view.seller;

import it.unicam.cs.ids2425.controller.article.actor.seller.EventPlannerArticleController;
import it.unicam.cs.ids2425.model.article.article.Event;
import it.unicam.cs.ids2425.controller.problem.ProblemController;
import it.unicam.cs.ids2425.controller.user.OtherUserController;
import it.unicam.cs.ids2425.view.SellerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller/event-planner")
public class EventPlannerView extends SellerView<Event> {
    @Autowired
    public EventPlannerView(OtherUserController userController, EventPlannerArticleController sellerController, ProblemController problemController) {
        super(userController, sellerController, problemController);
    }
}
