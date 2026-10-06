package com.bentotec.sistema_votacao.controller;

import com.bentotec.sistema_votacao.model.Evento;
import com.bentotec.sistema_votacao.service.EventoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Evento> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Evento buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
}