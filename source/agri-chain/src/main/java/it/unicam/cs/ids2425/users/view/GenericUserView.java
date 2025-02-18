package it.unicam.cs.ids2425.users.view;

import it.unicam.cs.ids2425.articles.controller.ArticleController;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.views.IView;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class GenericUserView implements IView {
    private final ArticleController articleController = SingletonController.getInstance(new ArticleController());

    public ViewResponse<List<IArticle>> getAll() {
        return genericCall(() -> {
                    List<IArticle> articles = articleController.getAll(new GenericUser() {
                    }, ArticleStatus.PUBLISHED);
                    return articles.isEmpty() ? null : articles;
                },
                ResponseStatus.OK,
                ResponseStatus.NOT_FOUND);
    }

    public ViewResponse<IArticle> get(IArticle article) {
        return genericCall(() -> articleController.get(article, ArticleStatus.PUBLISHED),
                ResponseStatus.OK,
                ResponseStatus.NOT_FOUND);
    }

    public ViewResponse<String> shareArticle(IArticle article) {
        return genericCall(() -> articleController.shareContent(article),
                ResponseStatus.OK,
                ResponseStatus.NOT_FOUND);
    }
}
