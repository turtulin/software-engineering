package it.unicam.cs.ids2425.repository.article;

import it.unicam.cs.ids2425.model.article.Article;
import it.unicam.cs.ids2425.model.article.ArticleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleStateRepository extends JpaRepository<ArticleState, Long> {

    List<ArticleState> findAllByEntity(Article entity);

    List<ArticleState> findAllByEntity_Id(Long articleId);
}
