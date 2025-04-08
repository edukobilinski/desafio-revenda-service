package com.desafio.revendaservice.api.controller;

import com.desafio.revendaservice.application.dto.*;
import com.desafio.revendaservice.application.service.RevendaService;
import com.desafio.revendaservice.domain.model.PedidoRevenda;
import com.desafio.revendaservice.domain.model.Revenda;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RevendaController.class)
class RevendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RevendaService revendaService;

    @Autowired
    private ObjectMapper objectMapper;

    private RevendaDto revendaDto;

    @BeforeEach
    void setup() {
        revendaDto = new RevendaDto(
            "12.345.678/0001-90",
            "Razão LTDA",
            "Fantasia",
            "email@empresa.com",
            List.of("11999999999"),
            List.of(new ContatoDto("João", true)),
            List.of(new EnderecoDto("Rua A", "123", "Cidade", "SP", "01000-000"))
        );
    }

    @Test
    void deveCadastrarRevenda() throws Exception {
        Revenda revenda = Revenda.builder().id(1L).cnpj(revendaDto.cnpj()).build();
        when(revendaService.cadastrarRevenda(any())).thenReturn(revenda);

        mockMvc.perform(post("/revendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(revendaDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deveReceberPedido() throws Exception {
        PedidoClienteDto pedidoDto = new PedidoClienteDto(
            123L, List.of(new ItemPedidoDto("Cerveja", 1000))
        );

        PedidoRevenda pedido = PedidoRevenda.builder().id(10L).clienteId("123").revendaId(1L).build();

        when(revendaService.receberPedido(eq(1L), any())).thenReturn(pedido);

        mockMvc.perform(post("/revendas/1/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void deveListarTodasAsRevendas() throws Exception {
        when(revendaService.buscarTodas()).thenReturn(List.of(new Revenda()));

        mockMvc.perform(get("/revendas"))
            .andExpect(status().isOk());
    }

    @Test
    void deveBuscarRevendaPorId() throws Exception {
        Revenda revenda = Revenda.builder().id(5L).build();
        when(revendaService.buscarPorId(5L)).thenReturn(revenda);

        mockMvc.perform(get("/revendas/5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(5L));
    }

    @Test
    void deveListarPedidosPorRevenda() throws Exception {
        when(revendaService.listarPedidosPorRevenda(1L)).thenReturn(List.of(new PedidoRevenda()));

        mockMvc.perform(get("/revendas/1/pedidos"))
            .andExpect(status().isOk());
    }
}
