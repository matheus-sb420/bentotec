package com.bentotec.sistema_votacao.dto;

public record ResultadoProjetoResponse(

        Long projetoId,

        String projeto,

        long votos

) {
}