package com.desafio.revendaservice.application.service;

import com.desafio.revendaservice.application.client.PedidoClient;
import com.desafio.revendaservice.application.dto.PedidoResponse;
import com.desafio.revendaservice.domain.model.PedidoPendente;
import com.desafio.revendaservice.domain.model.PedidoRevenda;
import com.desafio.revendaservice.domain.model.ItemPedido;
import com.desafio.revendaservice.infrastructure.repository.PedidoPendenteRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    private final PedidoClient client;
    private final PedidoPendenteRepository pendenteRepository;

    @Autowired
    public PedidoService(PedidoClient client, PedidoPendenteRepository pendenteRepository) {
        this.client = client;
        this.pendenteRepository = pendenteRepository;
    }

    @Retry(name = "revendaPedido", fallbackMethod = "fallback")
    public PedidoResponse emitirPedido(PedidoRevenda pedido) {
        int total = pedido.getItens().stream().mapToInt(ItemPedido::getQuantidade).sum();
        if (total < 1000) {
            throw new IllegalArgumentException("Pedido não atinge o mínimo de 1000 unidades");
        }
        return client.emitir(pedido);
    }

    public PedidoResponse fallback(PedidoRevenda pedido, Throwable ex) {
        PedidoPendente pendente = PedidoPendente.builder()
            .pedidoOriginalId(pedido.getId())
            .itens(pedido.getItens())
            .build();

        pendenteRepository.save(pendente);

        return new PedidoResponse("PENDENTE", null);
    }
}