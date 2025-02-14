package it.unicam.cs.ids2425.core.model;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static final Map<String, AtomicLong> sequences = new ConcurrentHashMap<>();

    public static <T extends IIdentifier> T create(Class<T> type, String context) {
        String prefix = type.getSimpleName().replace("Id", "");
        String id = String.format("%s-%s-%tF-%04d",
                prefix,
                context,
                LocalDate.now(),
                sequences.computeIfAbsent(prefix, k -> new AtomicLong()).getAndIncrement()
        );
        return createId(type, id);
    }

    static <T extends IIdentifier> T createId(Class<T> type, String value) {
        try {
            return type.getConstructor(String.class).newInstance(value);
        } catch (Exception e) {
            throw new RuntimeException("Invalid ID type: " + type.getName(), e);
        }
    }
}
