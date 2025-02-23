package it.unicam.cs.ids2425.article.repository;

import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.model.ArticleState;
import it.unicam.cs.ids2425.utilities.statuscode.BaseStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleStateRepository extends JpaRepository<ArticleState, Long> {
    List<ArticleState> findAllByStatusCode(BaseStatusCode articleStatusCode);

    List<ArticleState> findAllByEntity(Article entity);

    List<ArticleState> findAllByEntity_Id(Long articleId);

    List<ArticleState> findAllByEntityAndStatusCode(Article article, BaseStatusCode articleStatusCode);
}
