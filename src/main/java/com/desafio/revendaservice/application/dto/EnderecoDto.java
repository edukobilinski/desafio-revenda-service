package com.desafio.revendaservice.application.dto;

import jakarta.validation.constraints.NotBlank;

public record EnderecoDto(
    @NotBlank String rua,
    @NotBlank String numero,
    @NotBlank String cidade,
    @NotBlank String estado,
    @NotBlank String cep
) {}