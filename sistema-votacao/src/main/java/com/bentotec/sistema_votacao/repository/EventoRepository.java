package com.bentotec.sistema_votacao.repository;

import com.bentotec.sistema_votacao.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}