package com.desafio.revendaservice.api.controller;

import com.desafio.revendaservice.application.service.PedidoService;
import com.desafio.revendaservice.domain.model.PedidoPendente;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoPendenteController.class)
class PedidoPendenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveReprocessarPedidosPendentes() throws Exception {
        PedidoPendente pendente = PedidoPendente.builder()
            .id(1L)
            .pedidoOriginalId(100L)
            .build();

        when(pedidoService.reprocessarPedidosPendentes())
            .thenReturn(List.of(pendente));

        mockMvc.perform(post("/pedidos-pendentes/reprocessar")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].id").value(1));
    }
}
