package it.unicam.cs.ids2425.article.repository;

import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllBySeller(User seller);

    Optional<Article> findByName(String name);
}
