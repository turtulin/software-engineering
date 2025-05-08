package it.unicam.cs.ids2425.user.view.seller;

import it.unicam.cs.ids2425.article.controller.actor.seller.TransformerArticleController;
import it.unicam.cs.ids2425.article.model.article.compositearticle.ProcessedProduct;
import it.unicam.cs.ids2425.problem.controller.ProblemController;
import it.unicam.cs.ids2425.user.controller.actor.OtherUserController;
import it.unicam.cs.ids2425.user.view.SellerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller/transformer")

public class TransformerView extends SellerView<ProcessedProduct> {
    @Autowired
    public TransformerView(OtherUserController userController, TransformerArticleController sellerController, ProblemController problemController) {
        super(userController, sellerController, problemController);
    }
}
