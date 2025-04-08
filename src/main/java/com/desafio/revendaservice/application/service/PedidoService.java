package com.desafio.revendaservice.application.service;

import com.desafio.revendaservice.api.exception.PedidoInvalidoException;
import com.desafio.revendaservice.application.client.PedidoClient;
import com.desafio.revendaservice.application.dto.PedidoResponse;
import com.desafio.revendaservice.domain.model.ItemPedido;
import com.desafio.revendaservice.domain.model.PedidoPendente;
import com.desafio.revendaservice.domain.model.PedidoRevenda;
import com.desafio.revendaservice.infrastructure.repository.PedidoPendenteRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoClient client;
    private final PedidoPendenteRepository pedidoPendenteRepository;

    @Autowired
    public PedidoService(PedidoClient client, PedidoPendenteRepository pedidoPendenteRepository) {
        this.client = client;
        this.pedidoPendenteRepository = pedidoPendenteRepository;
    }

    @Retry(name = "revendaPedido", fallbackMethod = "fallback")
    public PedidoResponse emitirPedido(PedidoRevenda pedido) {
        log.info("Iniciando emissão de pedido para revendaId={}, clienteId={}",
            pedido.getRevendaId(), pedido.getClienteId());

        validarPedidoMinimo(pedido);

        PedidoResponse response = client.emitir(pedido);
        log.info("Pedido emitido com sucesso: numeroPedido={}", response.numeroPedido());

        return response;
    }

    public PedidoResponse fallback(PedidoRevenda pedido) {
        log.warn("Fallback acionado para pedido da revendaId={} - salvando como pendente", pedido.getRevendaId());

        PedidoPendente pendente = PedidoPendente.builder()
            .pedidoOriginalId(pedido.getId())
            .itens(pedido.getItens())
            .build();

        pedidoPendenteRepository.save(pendente);

        return new PedidoResponse("PENDENTE", null);
    }

    public List<PedidoPendente> reprocessarPedidosPendentes() {
        log.info("Reprocessando pedidos pendentes...");
        List<PedidoPendente> pendentes = pedidoPendenteRepository.findAll();
        List<PedidoPendente> processados = new ArrayList<>();

        for (PedidoPendente pendente : pendentes) {
            try {
                PedidoRevenda novoPedido = reconstruirPedido(pendente);
                PedidoResponse response = emitirPedido(novoPedido);

                if (!"PENDENTE".equals(response.numeroPedido())) {
                    pedidoPendenteRepository.delete(pendente);
                    processados.add(pendente);
                    log.info("Pedido pendente reprocessado com sucesso e removido. ID original={}",
                        pendente.getPedidoOriginalId());
                }
            } catch (Exception ex) {
                log.error("Erro ao reprocessar pedido pendente. ID original={}, motivo={}",
                    pendente.getPedidoOriginalId(), ex.getMessage());
            }
        }

        log.info("Total de pedidos reprocessados com sucesso: {}", processados.size());
        return processados;
    }

    private void validarPedidoMinimo(PedidoRevenda pedido) {
        int total = pedido.getItens().stream()
            .mapToInt(ItemPedido::getQuantidade)
            .sum();

        log.debug("Total de itens no pedido: {}", total);

        if (total < 1000) {
            throw new PedidoInvalidoException("Pedido não atinge o mínimo de 1000 unidades");
        }
    }

    private PedidoRevenda reconstruirPedido(PedidoPendente pendente) {
        return PedidoRevenda.builder()
            .revendaId(pendente.getPedidoOriginalId())
            .itens(pendente.getItens())
            .enviado(false)
            .build();
    }
}
