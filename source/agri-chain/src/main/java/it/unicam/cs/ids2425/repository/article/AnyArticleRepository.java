package it.unicam.cs.ids2425.repository.article;

import it.unicam.cs.ids2425.model.article.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface AnyArticleRepository extends ArticleRepository<Article> {
}
