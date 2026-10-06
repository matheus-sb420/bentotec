package com.bentotec.sistema_votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException exception) {

        HttpStatus status = switch (exception.getCode()) {

            case EVENTO_NAO_ENCONTRADO,
                 PROJETO_NAO_ENCONTRADO ->
                    HttpStatus.NOT_FOUND;

            case VOTO_JA_REGISTRADO ->
                    HttpStatus.CONFLICT;

            case EVENTO_FECHADO ->
                    HttpStatus.FORBIDDEN;

            case IDENTIDADE_INVALIDA,
                 CREDENCIAIS_INVALIDAS ->
                    HttpStatus.UNAUTHORIZED;

            case DADOS_INVALIDOS ->
                    HttpStatus.BAD_REQUEST;
        };

        return ResponseEntity
                .status(status)
                .body(Map.of(
                        "codigo", exception.getCode().name(),
                        "mensagem", exception.getMessage(),
                        "timestamp", LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException exception) {

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "codigo", ErrorCode.DADOS_INVALIDOS.name(),
                        "mensagem", "Dados inválidos.",
                        "timestamp", LocalDateTime.now()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(
            IllegalArgumentException exception) {

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "codigo", ErrorCode.DADOS_INVALIDOS.name(),
                        "mensagem", "Dados inválidos.",
                        "timestamp", LocalDateTime.now()
                ));
    }
}