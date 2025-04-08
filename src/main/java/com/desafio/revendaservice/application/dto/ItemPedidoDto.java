package com.desafio.revendaservice.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ItemPedidoDto(
    @NotBlank String produto,
    @Min(1) int quantidade
) {}