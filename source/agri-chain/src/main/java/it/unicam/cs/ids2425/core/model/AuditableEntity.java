package it.unicam.cs.ids2425.core.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class AuditableEntity {
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected AuditableEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    protected void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
