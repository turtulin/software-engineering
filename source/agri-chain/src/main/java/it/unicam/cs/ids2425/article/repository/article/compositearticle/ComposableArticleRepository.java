package it.unicam.cs.ids2425.article.repository.article.compositearticle;

import it.unicam.cs.ids2425.article.model.article.compositearticle.ComposableArticle;
import it.unicam.cs.ids2425.article.repository.ArticleRepository;

public interface ComposableArticleRepository<T extends ComposableArticle> extends ArticleRepository<T> {
}
