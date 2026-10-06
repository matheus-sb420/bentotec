package com.bentotec.sistema_votacao.controller;

import com.bentotec.sistema_votacao.dto.VotoRequest;
import com.bentotec.sistema_votacao.dto.VotoResponse;
import com.bentotec.sistema_votacao.service.VotoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/votos")
public class VotoController {

    private final VotoService service;

    public VotoController(VotoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VotoResponse> votar(
            @RequestBody @Valid VotoRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        UUID identidadeUuid = null;

        if (jwt != null) {

            String tipo =
                    jwt.getClaimAsString("tipo");

            if (!"VISITANTE".equals(tipo)) {

                return ResponseEntity
                        .status(401)
                        .build();
            }

            try {

                identidadeUuid =
                        UUID.fromString(jwt.getSubject());

            } catch (IllegalArgumentException exception) {

                return ResponseEntity
                        .status(401)
                        .build();
            }
        }

        VotoResponse response =
                service.votar(
                        request,
                        identidadeUuid
                );

        return ResponseEntity
                .status(201)
                .body(response);
    }
}