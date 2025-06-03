package it.unicam.cs.ids2425.article.controller;

import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.model.ArticleState;
import it.unicam.cs.ids2425.article.repository.ArticleRepository;
import it.unicam.cs.ids2425.article.repository.ArticleStateRepository;
import it.unicam.cs.ids2425.user.model.User;
import it.unicam.cs.ids2425.user.model.UserRole;
import it.unicam.cs.ids2425.utilities.statuscode.specificstatuscode.ArticleStatusCode;
import jakarta.transaction.Transactional;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


public abstract class AbstractArticleController<T extends Article> {
    protected final ArticleStateRepository articleStateRepository;
    protected final ArticleRepository<T> articleRepository;

    public AbstractArticleController(ArticleStateRepository articleStateRepository, ArticleRepository<T> articleRepository) {
        this.articleStateRepository = articleStateRepository;
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles(@NonNull ArticleStatusCode articleStatusCode) {
        return articleRepository.findAll().stream().map(a -> articleStateRepository.findAllByEntity_Id(a.getId()).getLast()).filter(st -> st.getStatusCode().equals(articleStatusCode)).map(ArticleState::getEntity).toList();
    }

    @Transactional
    public Article getArticleById(@NonNull Long id, @NonNull ArticleStatusCode articleStatusCode) {
        try {
            ArticleState articleState = articleStateRepository.findAllByEntity_Id(id).getLast();
            if (!articleState.getStatusCode().equals(articleStatusCode)) {
                throw new IllegalArgumentException("Article not found");
            }
            return articleState.getEntity();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Article not found");
        }
    }

    @Transactional
    public Map<String, String> shareArticle(@NonNull Long id, @NonNull ArticleStatusCode articleStatusCode) {
        Map<String, String> response = new HashMap<>();
        response.put("uri", STR."/article/\{id}");
        response.put("article", getArticleById(id, articleStatusCode).toString());
        return response;
    }

    public Article approveArticle(@NonNull Long id, @NonNull User user) {
        return evaluateArticle(id, ArticleStatusCode.PUBLISHED, user);
    }

    public Article rejectArticle(@NonNull Long id, @NonNull User user) {
        return evaluateArticle(id, ArticleStatusCode.REJECTED, user);
    }

    private Article getArticleById(@NonNull Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Article not found"));
    }

    private Article evaluateArticle(@NonNull Long id, @NonNull ArticleStatusCode newArticleStatusCode, User user) {
        if (user.getRole() != UserRole.MODERATOR) {
            throw new IllegalArgumentException("User must be a moderator");
        }
        Article article = getArticleById(id);
        ArticleState oldState = articleStateRepository.findAllByEntity(article).getLast();
        if (!oldState.getStatusCode().equals(ArticleStatusCode.PENDING)) {
            throw new IllegalArgumentException("Article must be pending");
        }
        articleStateRepository.save(new ArticleState(newArticleStatusCode, user, "Rejected", article, oldState));
        return article;
    }

    public abstract List<Article> getAllArticles(@NonNull ArticleStatusCode articleStatusCode, User user);
}
