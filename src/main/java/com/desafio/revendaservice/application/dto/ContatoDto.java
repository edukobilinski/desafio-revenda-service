package com.desafio.revendaservice.application.dto;

import jakarta.validation.constraints.NotBlank;

public record ContatoDto(
    @NotBlank String nome,
    boolean principal
) {}