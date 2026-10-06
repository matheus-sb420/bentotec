package com.bentotec.sistema_votacao.service;

import com.bentotec.sistema_votacao.dto.EventoRequest;
import com.bentotec.sistema_votacao.exception.ApiException;
import com.bentotec.sistema_votacao.exception.ErrorCode;
import com.bentotec.sistema_votacao.model.Evento;
import com.bentotec.sistema_votacao.model.StatusEvento;
import com.bentotec.sistema_votacao.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository repository;

    public EventoService(EventoRepository repository) {
        this.repository = repository;
    }

    public Evento criar(EventoRequest request) {

        if (!request.dataFim().isAfter(
                request.dataInicio())) {

            throw new ApiException(
                    ErrorCode.DADOS_INVALIDOS,
                    "A data final deve ser posterior à data inicial."
            );
        }

        if (request.dataInicio().isBefore(
                LocalDateTime.now())) {

            throw new ApiException(
                    ErrorCode.DADOS_INVALIDOS,
                    "A data inicial não pode estar no passado."
            );
        }

        Evento evento = new Evento();

        evento.setNome(request.nome());
        evento.setDataInicio(request.dataInicio());
        evento.setDataFim(request.dataFim());
        evento.setStatus(StatusEvento.ABERTO);

        return repository.save(evento);
    }

    public List<Evento> listar() {
        return repository.findAll();
    }

    public Evento buscar(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                ErrorCode.EVENTO_NAO_ENCONTRADO,
                                "Evento não encontrado."
                        )
                );
    }

    public Evento encerrar(Long id) {

        Evento evento = buscar(id);

        evento.setStatus(StatusEvento.ENCERRADO);

        return repository.save(evento);
    }

    public boolean eventoEstaAberto(Evento evento) {

        LocalDateTime agora = LocalDateTime.now();

        return evento.getStatus() == StatusEvento.ABERTO
                && !agora.isBefore(evento.getDataInicio())
                && agora.isBefore(evento.getDataFim());
    }
}