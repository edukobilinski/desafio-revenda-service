package com.desafio.revendaservice.application.dto;

import java.util.List;

public record PedidoResponse(
    String numeroPedido,
    List<ItemPedidoDto> itens
) {}