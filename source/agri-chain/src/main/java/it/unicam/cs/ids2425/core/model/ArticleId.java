package it.unicam.cs.ids2425.core.model;

public record ArticleId(String value) implements IIdentifier {
    public ArticleId {
        if (!value.startsWith("Article")) {
            throw new IllegalArgumentException("Invalid Article ID format");
        }
    }
}
