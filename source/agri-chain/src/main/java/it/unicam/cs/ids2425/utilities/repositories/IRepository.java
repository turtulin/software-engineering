package it.unicam.cs.ids2425.utilities.repositories;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    T save(T entity);

    Optional<T> findById(T entity);

    List<T> findAll();

    void deleteById(T id);

    boolean existsById(T id);
}
