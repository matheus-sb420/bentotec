package com.bentotec.sistema_votacao.repository;

import com.bentotec.sistema_votacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByIdentidadeIdAndProjetoId(
            Long identidadeId,
            Long projetoId
    );

    long countByProjetoId(Long projetoId);
}