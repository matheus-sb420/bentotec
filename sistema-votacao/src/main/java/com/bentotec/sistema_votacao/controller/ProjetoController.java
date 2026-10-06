package com.bentotec.sistema_votacao.controller;

import com.bentotec.sistema_votacao.model.Projeto;
import com.bentotec.sistema_votacao.service.ProjetoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService service;

    public ProjetoController(ProjetoService service) {
        this.service = service;
    }

    @GetMapping("/evento/{eventoId}")
    public List<Projeto> listarPorEvento(
            @PathVariable Long eventoId) {

        return service.listarPorEvento(eventoId);
    }
}