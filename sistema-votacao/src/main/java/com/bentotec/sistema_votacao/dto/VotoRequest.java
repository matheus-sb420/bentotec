package com.bentotec.sistema_votacao.dto;

import jakarta.validation.constraints.NotNull;

public record VotoRequest(

        @NotNull
        Long projetoId

) {
}