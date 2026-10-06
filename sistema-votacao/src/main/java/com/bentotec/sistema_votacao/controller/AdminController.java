package com.bentotec.sistema_votacao.controller;

import com.bentotec.sistema_votacao.dto.*;
import com.bentotec.sistema_votacao.model.Evento;
import com.bentotec.sistema_votacao.model.Projeto;
import com.bentotec.sistema_votacao.service.AdminService;
import com.bentotec.sistema_votacao.service.EventoService;
import com.bentotec.sistema_votacao.service.ProjetoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final EventoService eventoService;
    private final ProjetoService projetoService;

    public AdminController(
            AdminService adminService,
            EventoService eventoService,
            ProjetoService projetoService) {

        this.adminService = adminService;
        this.eventoService = eventoService;
        this.projetoService = projetoService;
    }

    /*
     * LOGIN
     */

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody @Valid LoginRequest request) {

        return adminService.login(request);
    }

    /*
     * EVENTOS
     */

    @PostMapping("/eventos")
    public Evento criarEvento(
            @RequestBody @Valid EventoRequest request) {

        return eventoService.criar(request);
    }

    @PutMapping("/eventos/{id}/encerrar")
    public Evento encerrarEvento(
            @PathVariable Long id) {

        return eventoService.encerrar(id);
    }

    /*
     * PROJETOS
     */

    @PostMapping("/projetos")
    public Projeto criarProjeto(
            @RequestBody @Valid ProjetoRequest request) {

        return projetoService.criar(request);
    }

    /*
     * RESULTADOS
     */

    @GetMapping("/resultados/{eventoId}")
    public List<ResultadoProjetoResponse> resultados(
            @PathVariable Long eventoId) {

        return projetoService.resultados(eventoId);
    }
}