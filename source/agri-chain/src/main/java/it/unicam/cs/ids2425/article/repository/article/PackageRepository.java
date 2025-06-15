package it.unicam.cs.ids2425.article.repository.article;

import it.unicam.cs.ids2425.article.model.article.Package;
import it.unicam.cs.ids2425.article.repository.ArticleRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends ArticleRepository<Package> {
}
