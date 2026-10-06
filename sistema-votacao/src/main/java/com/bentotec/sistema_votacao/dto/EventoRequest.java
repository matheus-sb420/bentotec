package com.bentotec.sistema_votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventoRequest(

        @NotBlank
        String nome,

        @NotNull
        LocalDateTime dataInicio,

        @NotNull
        LocalDateTime dataFim

) {
}