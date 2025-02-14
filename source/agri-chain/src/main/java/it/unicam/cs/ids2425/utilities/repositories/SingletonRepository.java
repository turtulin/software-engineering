package it.unicam.cs.ids2425.utilities.repositories;

import it.unicam.cs.ids2425.utilities.wrappers.TypeToken;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class SingletonRepository<T extends Collection<S>, S, ID> implements IRepository<T, S, ID> {
    private static final Map<Class<?>, SingletonRepository<?, ?, ?>> instances = new HashMap<>();
    private final T entities;

    private SingletonRepository(Class<T> type) {
        try {
            this.entities = type.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Note: an alternative solution to warning suppression is way too long and adds a lot of data and code.
     * Therefore, I am suppressing a Warning that does never cause any problems.
     * instance is always an instance of SingletonRepository<T> because of the way it is created.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Collection<S>, S, ID> SingletonRepository<T, S, ID> getInstance(TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        return (SingletonRepository<T, S, ID>) instances.computeIfAbsent(type.getClass(), k -> new SingletonRepository<>(
                (Class<T>) type.getClass()));
    }
}