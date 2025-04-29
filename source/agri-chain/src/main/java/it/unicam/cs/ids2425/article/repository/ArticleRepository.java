package it.unicam.cs.ids2425.article.repository;

import it.unicam.cs.ids2425.article.model.Article;
import it.unicam.cs.ids2425.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository<T extends Article> extends JpaRepository<T, Long> {
    List<T> findAllBySeller(User seller);

    Optional<T> findByName(String name);
}
