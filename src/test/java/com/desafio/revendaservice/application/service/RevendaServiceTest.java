package com.desafio.revendaservice.application.service;

import com.desafio.revendaservice.api.exception.RevendaNotFoundException;
import com.desafio.revendaservice.application.dto.*;
import com.desafio.revendaservice.domain.model.*;
import com.desafio.revendaservice.infrastructure.repository.PedidoRevendaRepository;
import com.desafio.revendaservice.infrastructure.repository.RevendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RevendaServiceTest {

    private RevendaRepository revendaRepository;
    private PedidoRevendaRepository pedidoRepository;
    private PedidoService pedidoService;
    private RevendaService revendaService;

    @BeforeEach
    void setUp() {
        revendaRepository = mock(RevendaRepository.class);
        pedidoRepository = mock(PedidoRevendaRepository.class);
        pedidoService = mock(PedidoService.class);
        revendaService = new RevendaService(revendaRepository, pedidoRepository, pedidoService);
    }

    @Test
    void deveCadastrarNovaRevenda() {
        RevendaDto dto = new RevendaDto(
            "12.345.678/0001-90", "Razão LTDA", "Fantasia", "email@empresa.com",
            List.of("11999999999"),
            List.of(new ContatoDto("Fulano", true)),
            List.of(new EnderecoDto("Rua A", "123", "Cidade", "Estado", "00000-000"))
        );

        Revenda mockSaved = Revenda.builder().id(10L).cnpj(dto.cnpj()).build();
        when(revendaRepository.save(any())).thenReturn(mockSaved);

        Revenda result = revendaService.cadastrarRevenda(dto);

        assertThat(result.getId()).isEqualTo(10L);
        verify(revendaRepository).save(any(Revenda.class));
    }

    @Test
    void deveReceberPedidoDeCliente() {
        PedidoClienteDto pedidoDto = new PedidoClienteDto(
            123L,
            List.of(new ItemPedidoDto("Cerveja", 1000))
        );

        PedidoRevenda mockPedido = PedidoRevenda.builder().id(1L).clienteId("123").revendaId(1L).build();

        when(revendaRepository.existsById(1L)).thenReturn(true);
        when(pedidoRepository.save(any())).thenReturn(mockPedido);
        when(pedidoService.emitirPedido(any())).thenReturn(new PedidoResponse("OK", null));

        PedidoRevenda result = revendaService.receberPedido(1L, pedidoDto);

        assertThat(result.getId()).isEqualTo(1L);
        verify(pedidoService).emitirPedido(any());
        verify(pedidoRepository).save(any());
    }

    @Test
    void deveBuscarTodasRevendas() {
        when(revendaRepository.findAll()).thenReturn(List.of(new Revenda(), new Revenda()));

        List<Revenda> lista = revendaService.buscarTodas();

        assertThat(lista).hasSize(2);
    }

    @Test
    void deveBuscarRevendaPorIdComSucesso() {
        Revenda r = Revenda.builder().id(5L).build();
        when(revendaRepository.findById(5L)).thenReturn(Optional.of(r));

        Revenda result = revendaService.buscarPorId(5L);

        assertThat(result.getId()).isEqualTo(5L);
    }

    @Test
    void deveLancarExcecaoQuandoBuscarPorIdNaoEncontrado() {
        when(revendaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> revendaService.buscarPorId(99L))
            .isInstanceOf(RevendaNotFoundException.class)
            .hasMessageContaining("Revenda com id 99 não encontrada");
    }

    @Test
    void deveListarPedidosPorRevenda() {
        List<PedidoRevenda> pedidos = List.of(
            PedidoRevenda.builder().id(1L).revendaId(1L).build(),
            PedidoRevenda.builder().id(2L).revendaId(1L).build()
        );

        when(revendaRepository.existsById(1L)).thenReturn(true);
        when(pedidoRepository.findByRevendaId(1L)).thenReturn(pedidos);

        List<PedidoRevenda> result = revendaService.listarPedidosPorRevenda(1L);

        assertThat(result).hasSize(2);
        verify(pedidoRepository).findByRevendaId(1L);
    }

    @Test
    void deveLancarExcecaoAoListarPedidosDeRevendaInexistente() {
        when(revendaRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> revendaService.listarPedidosPorRevenda(99L))
            .isInstanceOf(RevendaNotFoundException.class)
            .hasMessageContaining("Revenda com id 99 não existe.");
    }
}
