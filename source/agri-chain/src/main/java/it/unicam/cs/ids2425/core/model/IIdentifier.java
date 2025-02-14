package it.unicam.cs.ids2425.core.model;

public sealed interface IIdentifier permits ArticleId, UserId {

    static <T extends IIdentifier> T parse(Class<T> type, String value) {
        String expectedPrefix = type.getSimpleName().replace("Id", "");
        if (!value.startsWith(expectedPrefix)) {
            throw new IllegalArgumentException("Invalid ID format for " + type.getSimpleName());
        }
        return IdGenerator.createId(type, value);
    }

    String value();

    default String prefix() {
        return value().split("-")[0];
    }
}
