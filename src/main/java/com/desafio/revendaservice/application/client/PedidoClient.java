package com.desafio.revendaservice.application.client;

import com.desafio.revendaservice.application.dto.PedidoResponse;
import com.desafio.revendaservice.domain.model.PedidoRevenda;
import org.springframework.stereotype.Component;

@Component
public class PedidoClient {

    public PedidoResponse emitir(PedidoRevenda pedido) {
        if (Math.random() < 0.5) {
            throw new RuntimeException("Falha na comunicação com a API da Ambev");
        }
        return new PedidoResponse("AMBEV-" + pedido.getId(), null);
    }
}