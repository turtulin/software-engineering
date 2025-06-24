package it.unicam.cs.ids2425.view.seller;

import it.unicam.cs.ids2425.controller.article.actor.seller.TransformerArticleController;
import it.unicam.cs.ids2425.model.article.article.compositearticle.ProcessedProduct;
import it.unicam.cs.ids2425.controller.problem.ProblemController;
import it.unicam.cs.ids2425.controller.user.OtherUserController;
import it.unicam.cs.ids2425.view.SellerView;
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
