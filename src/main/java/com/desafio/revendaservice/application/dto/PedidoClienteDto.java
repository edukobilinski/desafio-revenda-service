package com.desafio.revendaservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PedidoClienteDto(
    @NotBlank String clienteId,
    @NotEmpty List<ItemPedidoDto> itens
) {}