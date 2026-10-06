package com.bentotec.sistema_votacao.repository;

import com.bentotec.sistema_votacao.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    List<Projeto> findByEventoId(Long eventoId);
}