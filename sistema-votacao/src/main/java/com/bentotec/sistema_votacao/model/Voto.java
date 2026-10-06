package com.bentotec.sistema_votacao.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "tb_votos",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_voto_identidade_projeto",
                        columnNames = {
                                "identidade_id",
                                "projeto_id"
                        }
                )
        }
)
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "identidade_id", nullable = false)
    private IdentidadeAnonima identidade;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {

        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }

    public Voto() {
    }

    public Long getId() {
        return id;
    }

    public IdentidadeAnonima getIdentidade() {
        return identidade;
    }

    public void setIdentidade(IdentidadeAnonima identidade) {
        this.identidade = identidade;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}