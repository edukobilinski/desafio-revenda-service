package com.desafio.revendaservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Pedido feito por um cliente para a revenda")
public record PedidoClienteDto(

    @Schema(description = "Identificador do cliente", example = "123")
    @NotNull Long clienteId,

    @Schema(description = "Itens do pedido")
    @NotNull List<ItemPedidoDto> itens
) {}
