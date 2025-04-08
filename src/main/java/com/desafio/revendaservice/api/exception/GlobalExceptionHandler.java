package com.desafio.revendaservice.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RevendaNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(RevendaNotFoundException ex) {
        return new ResponseEntity<>(
            new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage()),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(PedidoInvalidoException.class)
    public ResponseEntity<ApiErrorResponse> handlePedidoInvalido(PedidoInvalidoException ex) {
        return new ResponseEntity<>(
            new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid Request", ex.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ApiIntegrationException.class)
    public ResponseEntity<ApiErrorResponse> handleApiError(ApiIntegrationException ex) {
        return new ResponseEntity<>(
            new ApiErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), "External API Error", ex.getMessage()),
            HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining("; "));

        return new ResponseEntity<>(
            new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation Error", msg),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        return new ResponseEntity<>(
            new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Error", ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
