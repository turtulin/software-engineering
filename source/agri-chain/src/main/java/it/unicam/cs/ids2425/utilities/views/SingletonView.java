package it.unicam.cs.ids2425.utilities.views;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SingletonView<T extends IView> {
    private static final Map<Class<?>, SingletonView<?>> instances = new HashMap<>();
    private final T view;

    @SuppressWarnings("unchecked")
    public static <T extends IView> T getInstance(T controller) {
        return (T) instances.computeIfAbsent(controller.getClass(), k -> new SingletonView<>(controller)).getView();
    }
}
