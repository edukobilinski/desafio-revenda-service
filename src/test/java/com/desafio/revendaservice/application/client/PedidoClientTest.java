package com.desafio.revendaservice.application.client;

import com.desafio.revendaservice.api.exception.ApiIntegrationException;
import com.desafio.revendaservice.application.dto.PedidoResponse;
import com.desafio.revendaservice.domain.model.ItemPedido;
import com.desafio.revendaservice.domain.model.PedidoRevenda;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PedidoClientTest {

    private final PedidoClient client = new PedidoClient();

    @Test
    void deveRetornarPedidoQuandoNaoFalhar() {
        PedidoRevenda pedido = PedidoRevenda.builder()
            .id(123L)
            .itens(List.of(ItemPedido.builder().produto("Cerveja").quantidade(1000).build()))
            .build();

        boolean sucesso = false;
        for (int i = 0; i < 10; i++) {
            try {
                PedidoResponse response = client.emitir(pedido);
                assertThat(response.numeroPedido()).isEqualTo("AMBEV-" + pedido.getId());
                sucesso = true;
                break;
            } catch (ApiIntegrationException ignored) {
            }
        }

        assertThat(sucesso).isTrue();
    }

    @RepeatedTest(5)
    void deveLancarExcecaoQuandoFalhaNaIntegracao() {
        PedidoRevenda pedido = PedidoRevenda.builder()
            .id(99L)
            .itens(List.of(ItemPedido.builder().produto("Refri").quantidade(1000).build()))
            .build();

        try {
            client.emitir(pedido);
        } catch (ApiIntegrationException ex) {
            assertThat(ex.getMessage()).contains("Falha na comunicação");
            return;
        }
    }
}
