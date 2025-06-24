package it.unicam.cs.ids2425.repository.article.article.compositearticle;

import it.unicam.cs.ids2425.model.article.article.compositearticle.ComposableArticle;
import it.unicam.cs.ids2425.repository.article.ArticleRepository;

public interface ComposableArticleRepository<T extends ComposableArticle> extends ArticleRepository<T> {
}
