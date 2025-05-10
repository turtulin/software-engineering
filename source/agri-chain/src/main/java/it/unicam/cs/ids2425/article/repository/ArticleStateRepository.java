package it.unicam.cs.ids2425.article.repository;

import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.article.model.ArticleState;
import it.unicam.cs.ids2425.utilities.statuscode.BaseStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleStateRepository extends JpaRepository<ArticleState, Long> {
    List<ArticleState> findAllByStatusCode(BaseStatusCode articleStatusCode);

    List<ArticleState> findAllByEntity(Article entity);

    List<ArticleState> findAllByEntity_Id(Long articleId);
}
