package com.bentotec.sistema_votacao.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_identidades_anonimas")
public class IdentidadeAnonima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadaEm;

    @PrePersist
    public void prePersist() {

        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        if (criadaEm == null) {
            criadaEm = LocalDateTime.now();
        }
    }

    public IdentidadeAnonima() {
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }
}