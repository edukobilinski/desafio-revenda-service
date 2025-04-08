package com.desafio.revendaservice.application.service;

import com.desafio.revendaservice.api.exception.PedidoInvalidoException;
import com.desafio.revendaservice.application.client.PedidoClient;
import com.desafio.revendaservice.application.dto.PedidoResponse;
import com.desafio.revendaservice.domain.model.ItemPedido;
import com.desafio.revendaservice.domain.model.PedidoPendente;
import com.desafio.revendaservice.domain.model.PedidoRevenda;
import com.desafio.revendaservice.infrastructure.repository.PedidoPendenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    private PedidoClient client;
    private PedidoPendenteRepository repository;
    private PedidoService service;

    @BeforeEach
    void setUp() {
        client = mock(PedidoClient.class);
        repository = mock(PedidoPendenteRepository.class);
        service = new PedidoService(client, repository);
    }

    @Test
    void deveEmitirPedidoComSucesso() {
        PedidoRevenda pedido = PedidoRevenda.builder()
            .revendaId(1L)
            .clienteId("2")
            .itens(List.of(ItemPedido.builder().produto("Cerveja").quantidade(1000).build()))
            .build();

        when(client.emitir(pedido)).thenReturn(new PedidoResponse("AMBEV-123", null));

        PedidoResponse response = service.emitirPedido(pedido);

        assertThat(response.numeroPedido()).isEqualTo("AMBEV-123");
    }

    @Test
    void deveLancarExcecaoSePedidoForMenorQueMinimo() {
        PedidoRevenda pedido = PedidoRevenda.builder()
            .revendaId(1L)
            .itens(List.of(ItemPedido.builder().produto("Cerveja").quantidade(10).build()))
            .build();

        assertThatThrownBy(() -> service.emitirPedido(pedido))
            .isInstanceOf(PedidoInvalidoException.class)
            .hasMessageContaining("mínimo de 1000");
    }

    @Test
    void fallbackDeveSalvarPedidoPendente() {
        PedidoRevenda pedido = PedidoRevenda.builder()
            .id(99L)
            .revendaId(1L)
            .itens(List.of(ItemPedido.builder().produto("Refrigerante").quantidade(500).build()))
            .build();

        PedidoResponse response = service.fallback(pedido);

        verify(repository).save(any(PedidoPendente.class));
        assertThat(response.numeroPedido()).isEqualTo("PENDENTE");
    }
}
