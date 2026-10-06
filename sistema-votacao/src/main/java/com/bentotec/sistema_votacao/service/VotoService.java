package com.bentotec.sistema_votacao.service;

import com.bentotec.sistema_votacao.dto.VotoRequest;
import com.bentotec.sistema_votacao.dto.VotoResponse;
import com.bentotec.sistema_votacao.exception.ApiException;
import com.bentotec.sistema_votacao.exception.ErrorCode;
import com.bentotec.sistema_votacao.model.*;
import com.bentotec.sistema_votacao.repository.IdentidadeAnonimaRepository;
import com.bentotec.sistema_votacao.repository.ProjetoRepository;
import com.bentotec.sistema_votacao.repository.VotoRepository;
import com.bentotec.sistema_votacao.security.JwtService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final ProjetoRepository projetoRepository;
    private final IdentidadeAnonimaRepository identidadeRepository;
    private final JwtService jwtService;
    private final EventoService eventoService;

    public VotoService(
            VotoRepository votoRepository,
            ProjetoRepository projetoRepository,
            IdentidadeAnonimaRepository identidadeRepository,
            JwtService jwtService,
            EventoService eventoService) {

        this.votoRepository = votoRepository;
        this.projetoRepository = projetoRepository;
        this.identidadeRepository = identidadeRepository;
        this.jwtService = jwtService;
        this.eventoService = eventoService;
    }

    @Transactional
    public VotoResponse votar(
            VotoRequest request,
            UUID identidadeUuid) {

        Projeto projeto = projetoRepository
                .findById(request.projetoId())
                .orElseThrow(() ->
                        new ApiException(
                                ErrorCode.PROJETO_NAO_ENCONTRADO,
                                "Projeto não encontrado."
                        )
                );

        Evento evento = projeto.getEvento();

        if (!eventoService.eventoEstaAberto(evento)) {

            throw new ApiException(
                    ErrorCode.EVENTO_FECHADO,
                    "O evento está fechado ou fora do período de votação."
            );
        }

        boolean novaIdentidade =
                identidadeUuid == null;

        IdentidadeAnonima identidade;

        /*
         * PRIMEIRO VOTO
         */
        if (novaIdentidade) {

            identidade =
                    identidadeRepository.save(
                            new IdentidadeAnonima()
                    );

        } else {

            identidade =
                    identidadeRepository
                            .findByUuid(identidadeUuid)
                            .orElseThrow(() ->
                                    new ApiException(
                                            ErrorCode.IDENTIDADE_INVALIDA,
                                            "Identidade anônima inválida."
                                    )
                            );
        }

        /*
         * PRIMEIRA VERIFICAÇÃO
         *
         * Serve para evitar uma tentativa
         * desnecessária de INSERT.
         */
        if (votoRepository
                .existsByIdentidadeIdAndProjetoId(
                        identidade.getId(),
                        projeto.getId())) {

            throw new ApiException(
                    ErrorCode.VOTO_JA_REGISTRADO,
                    "Você já votou neste projeto."
            );
        }

        Voto voto = new Voto();

        voto.setIdentidade(identidade);
        voto.setProjeto(projeto);

        try {

            votoRepository.saveAndFlush(voto);

        } catch (DataIntegrityViolationException exception) {

            /*
             * A constraint UNIQUE do PostgreSQL
             * continua sendo a barreira definitiva
             * contra corrida/concorrrência.
             */

            throw new ApiException(
                    ErrorCode.VOTO_JA_REGISTRADO,
                    "Você já votou neste projeto."
            );
        }

        /*
         * O token somente é devolvido no primeiro voto.
         */
        String token = null;

        if (novaIdentidade) {

            token = jwtService.gerarTokenVisitante(
                    identidade.getUuid(),
                    evento.getDataFim()
            );
        }

        return new VotoResponse(
                "VOTO_REGISTRADO",
                "Voto registrado com sucesso.",
                token
        );
    }
}