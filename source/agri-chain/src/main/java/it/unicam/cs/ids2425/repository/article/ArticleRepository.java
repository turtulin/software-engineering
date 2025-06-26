package it.unicam.cs.ids2425.repository.article;

import it.unicam.cs.ids2425.model.article.Article;
import it.unicam.cs.ids2425.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository<T extends Article> extends JpaRepository<T, Long> {
    List<T> findAllBySeller(User seller);

    Optional<T> findByName(String name);
}
