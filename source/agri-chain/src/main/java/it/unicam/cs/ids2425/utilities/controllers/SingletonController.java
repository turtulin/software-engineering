package it.unicam.cs.ids2425.utilities.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class SingletonController<T extends IController> {
    private static final Map<Class<?>, SingletonController<?>> instances = new HashMap<>();
    private final T controller;
    @SuppressWarnings("unchecked")
    public static <T extends IController> T getInstance(T controller) {
        return (T) instances.computeIfAbsent(controller.getClass(), k -> new SingletonController<>(controller)).getController();
    }
}
