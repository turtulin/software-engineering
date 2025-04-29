package it.unicam.cs.ids2425.article.repository;

import it.unicam.cs.ids2425.article.model.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface AnyArticleRepository extends ArticleRepository<Article> {
}
