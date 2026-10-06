package com.bentotec.sistema_votacao.repository;

import com.bentotec.sistema_votacao.model.IdentidadeAnonima;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IdentidadeAnonimaRepository
        extends JpaRepository<IdentidadeAnonima, Long> {

    Optional<IdentidadeAnonima> findByUuid(UUID uuid);
}