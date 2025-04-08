package com.desafio.revendaservice.api.exception;

public class PedidoInvalidoException extends RuntimeException {

    public PedidoInvalidoException(String message) {
        super(message);
    }
}
