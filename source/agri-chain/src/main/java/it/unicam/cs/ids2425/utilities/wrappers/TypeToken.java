package it.unicam.cs.ids2425.utilities.wrappers;

import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Getter
public abstract class TypeToken<T> {
    private final Type type;

    protected TypeToken() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }
}
