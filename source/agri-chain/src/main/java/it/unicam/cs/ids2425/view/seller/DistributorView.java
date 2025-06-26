package it.unicam.cs.ids2425.view.seller;

import it.unicam.cs.ids2425.controller.article.actor.seller.DistributorArticleController;
import it.unicam.cs.ids2425.model.article.article.Package;
import it.unicam.cs.ids2425.controller.problem.ProblemController;
import it.unicam.cs.ids2425.controller.user.OtherUserController;
import it.unicam.cs.ids2425.view.SellerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller/distributor")
public class DistributorView extends SellerView<Package> {
    @Autowired
    public DistributorView(OtherUserController userController, DistributorArticleController sellerController, ProblemController problemController) {
        super(userController, sellerController, problemController);
    }
}
