package it.unicam.cs.ids2425.core.model;

public record UserId(String value) implements IIdentifier {
    public UserId {
        if (!value.startsWith("User")) {
            throw new IllegalArgumentException("Invalid User ID format");
        }
    }
}
