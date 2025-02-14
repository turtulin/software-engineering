package it.unicam.cs.ids2425.utilities.repositories;

import java.util.Collection;

public interface IRepository<T extends Collection<S>, S, ID> {

    T getEntities();

    default T getAll() {
        return getEntities();
    }

    default S get(ID entity) {
        return getEntities().stream().filter(e -> e.equals(entity)).findFirst().orElse(null);
    }

    default boolean remove(ID entity) {
        S entityElem = get(entity);
        return getEntities().remove(entityElem);
    }

    default boolean create(S elem) {
        return getEntities().add(elem);
    }

    default S save(ID entity, S elem) {
        S entityElem = get(entity);
        if (entityElem != null) {
            remove(entity);
        }
        create(elem);
        return get(entity);
    }
}
