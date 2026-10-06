package com.bentotec.sistema_votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjetoRequest(

        @NotBlank
        String nome,

        String descricao,

        @NotNull
        Long eventoId

) {
}