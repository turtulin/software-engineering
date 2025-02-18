package it.unicam.cs.ids2425.utilities.repositories;

import it.unicam.cs.ids2425.core.identifiers.Identifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonRepository<T extends Identifiable<?>> implements IRepository<T> {
    private static final ConcurrentHashMap<Class<?>, SingletonRepository<?>> instances = new ConcurrentHashMap<>();
    private final List<T> storage = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public static <T extends Identifiable<?>> SingletonRepository<T> getInstance(Class<T> entityType) {
        return (SingletonRepository<T>) instances.computeIfAbsent(
                entityType,
                k -> new SingletonRepository<T>()
        );
    }

    @Override
    public T save(T entity) {
        storage.add(entity);
        return entity;
    }

    @Override
    public Optional<T> findById(T entity) {
        return storage.stream().filter(e -> e.equals(entity)).findFirst().or(Optional::empty);
    }

    @Override
    public List<T> findAll() {
        return List.copyOf(storage);
    }

    @Override
    public void deleteById(T entity) {
        storage.remove(entity);
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }

    @Override
    public boolean existsById(T entity) {
        return storage.contains(entity);
    }
}