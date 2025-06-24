package it.unicam.cs.ids2425.repository.article.article;

import it.unicam.cs.ids2425.model.article.article.Event;
import it.unicam.cs.ids2425.repository.article.ArticleRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends ArticleRepository<Event> {
}
