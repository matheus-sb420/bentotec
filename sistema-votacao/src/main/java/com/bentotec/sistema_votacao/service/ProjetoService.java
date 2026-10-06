package com.bentotec.sistema_votacao.service;

import com.bentotec.sistema_votacao.dto.ProjetoRequest;
import com.bentotec.sistema_votacao.dto.ResultadoProjetoResponse;
import com.bentotec.sistema_votacao.exception.ApiException;
import com.bentotec.sistema_votacao.exception.ErrorCode;
import com.bentotec.sistema_votacao.model.Evento;
import com.bentotec.sistema_votacao.model.Projeto;
import com.bentotec.sistema_votacao.model.StatusEvento;
import com.bentotec.sistema_votacao.repository.EventoRepository;
import com.bentotec.sistema_votacao.repository.ProjetoRepository;
import com.bentotec.sistema_votacao.repository.VotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final EventoRepository eventoRepository;
    private final VotoRepository votoRepository;

    public ProjetoService(
            ProjetoRepository projetoRepository,
            EventoRepository eventoRepository,
            VotoRepository votoRepository) {

        this.projetoRepository = projetoRepository;
        this.eventoRepository = eventoRepository;
        this.votoRepository = votoRepository;
    }

    public Projeto criar(ProjetoRequest request) {

        Evento evento = eventoRepository
                .findById(request.eventoId())
                .orElseThrow(() ->
                        new ApiException(
                                ErrorCode.EVENTO_NAO_ENCONTRADO,
                                "Evento não encontrado."
                        )
                );

        if (evento.getStatus() ==
                StatusEvento.ENCERRADO) {

            throw new ApiException(
                    ErrorCode.EVENTO_FECHADO,
                    "O evento está encerrado."
            );
        }

        Projeto projeto = new Projeto();

        projeto.setNome(request.nome());
        projeto.setDescricao(request.descricao());
        projeto.setEvento(evento);

        return projetoRepository.save(projeto);
    }

    public List<Projeto> listarPorEvento(Long eventoId) {

        if (!eventoRepository.existsById(eventoId)) {

            throw new ApiException(
                    ErrorCode.EVENTO_NAO_ENCONTRADO,
                    "Evento não encontrado."
            );
        }

        return projetoRepository.findByEventoId(eventoId);
    }

    public List<ResultadoProjetoResponse> resultados(
            Long eventoId) {

        if (!eventoRepository.existsById(eventoId)) {

            throw new ApiException(
                    ErrorCode.EVENTO_NAO_ENCONTRADO,
                    "Evento não encontrado."
            );
        }

        List<Projeto> projetos =
                projetoRepository.findByEventoId(eventoId);

        return projetos.stream()
                .map(projeto ->
                        new ResultadoProjetoResponse(
                                projeto.getId(),
                                projeto.getNome(),
                                votoRepository.countByProjetoId(
                                        projeto.getId()
                                )
                        )
                )
                .toList();
    }
}