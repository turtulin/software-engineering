package it.unicam.cs.ids2425.users.view.actors.sellers;

import it.unicam.cs.ids2425.articles.controller.ArticleController;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.users.controller.CanLoginController;
import it.unicam.cs.ids2425.users.controller.CanRegisterController;
import it.unicam.cs.ids2425.users.controller.actors.sellers.SellerController;
import it.unicam.cs.ids2425.users.model.actors.sellers.ISeller;
import it.unicam.cs.ids2425.users.view.CanLoginView;
import it.unicam.cs.ids2425.users.view.CanRegisterView;
import it.unicam.cs.ids2425.users.view.CanReportView;
import it.unicam.cs.ids2425.users.view.GenericUserView;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class SellerView extends GenericUserView implements CanRegisterView, CanLoginView, CanReportView {
    private final ArticleController articleController = SingletonController.getInstance(new ArticleController());

    public ViewResponse<List<IArticle>> getAll(ISeller seller) {
        return genericCall(() -> articleController.getAll(seller));
    }

    public ViewResponse<List<IArticle>> getAll(ISeller seller, ArticleStatus status) {
        return genericCall(() -> articleController.getAll(seller, status));
    }

    public ViewResponse<IArticle> get(IArticle article, ArticleStatus status) {
        return genericCall(() -> articleController.get(article, status));
    }


    public ViewResponse<IArticle> create(IArticle article, ISeller seller) {
        return genericCall(() -> articleController.create(article, seller),
                ResponseStatus.CREATED);
    }

    public ViewResponse<IArticle> updateArticle(IArticle article, ISeller seller) {
        return genericCall(() -> articleController.updateArticle(article, seller), ResponseStatus.ACCEPTED);
    }

    public ViewResponse<IArticle> deleteArticle(IArticle article, ISeller seller) {
        return genericCall(() -> articleController.deleteArticle(article, seller), ResponseStatus.ACCEPTED);
    }

    public ViewResponse<IArticle> publishArticle(IArticle article, ISeller seller) {
        return genericCall(() -> articleController.publishArticle(article, seller), ResponseStatus.ACCEPTED);
    }

    public ViewResponse<IArticle> draftArticle(IArticle article, ISeller seller) {
        return genericCall(() -> articleController.draftArticle(article, seller), ResponseStatus.ACCEPTED);
    }

    @Override
    public CanRegisterController getCanRegisterController() {
        return SingletonController.getInstance(new SellerController());
    }

    @Override
    public CanLoginController getCanLoginController() {
        return SingletonController.getInstance(new SellerController());
    }

    public ViewResponse<IArticle> setArticleQuantity(IArticle article, ISeller seller, int quantity) {
        return genericCall(() -> articleController.setArticleQuantity(article, seller, quantity));
    }

    public ViewResponse<Integer> getArticleQuantity(IArticle article, ISeller seller) {
        return genericCall(() -> articleController.getArticleQuantity(article, seller));
    }
}
