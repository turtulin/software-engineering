package it.unicam.cs.ids2425.user.view.seller;

import it.unicam.cs.ids2425.article.controller.actor.seller.EventPlannerArticleController;
import it.unicam.cs.ids2425.problem.controller.ProblemController;
import it.unicam.cs.ids2425.user.controller.actor.OtherUserController;
import it.unicam.cs.ids2425.user.view.SellerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller/event-planner")
public class EventPlannerView extends SellerView {
    @Autowired
    public EventPlannerView(OtherUserController userController, EventPlannerArticleController sellerController, ProblemController problemController) {
        super(userController, sellerController, problemController);
    }
}
