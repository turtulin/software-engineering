package it.unicam.cs.ids2425.users.view.actors.sellers;

import it.unicam.cs.ids2425.articles.controller.ArticleController;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.actors.sellers.SellerController;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import it.unicam.cs.ids2425.users.view.CanRegisterView;
import it.unicam.cs.ids2425.users.view.CanReportView;
import it.unicam.cs.ids2425.users.view.GenericUserView;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class SellerView extends GenericUserView implements CanRegisterView, CanReportView {
    private final ArticleController articleController = SingletonController.getInstance(new ArticleController() {
    });

    @Override
    public CanRegisterController getCanRegisterController() {
        return SingletonController.getInstance(new SellerController() {
        });
    }

    public ViewResponse<List<IArticle>> getAllArticles(ISeller seller) {
        return genericCall(() -> articleController.getAll(seller, null));
    }

    public ViewResponse<IArticle> create(IArticle article, ISeller seller) {
        return genericCall(() -> articleController.create(article, seller));
    }

    public ViewResponse<IArticle> updateArticle(IArticle article, ISeller seller) {
        return genericCall(() -> articleController.updateArticle(article, seller));
    }

    public ViewResponse<IArticle> deleteArticle(IArticle article, ISeller seller) {
        return genericCall(() -> articleController.deleteArticle(article, seller));
    }

    public ViewResponse<IArticle> publishArticle(IArticle article, ISeller seller) {
        return genericCall(() -> articleController.publishArticle(article, seller));
    }

    public ViewResponse<IArticle> draftArticle(IArticle article, ISeller seller) {
        return genericCall(() -> articleController.draftArticle(article, seller));
    }

    /*public ViewResponse<IArticle> reduceArticleQuantity(IArticle article, ISeller seller, int quantity) {
        return genericCall(() -> articleController.reduceArticleQuantity(article, seller, quantity));
    }*/
}
