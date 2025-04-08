package com.desafio.revendaservice.api.exception;

public class RevendaNotFoundException extends RuntimeException {

    public RevendaNotFoundException(String message) {
        super(message);
    }
}
