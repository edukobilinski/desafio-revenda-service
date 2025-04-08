package com.desafio.revendaservice.api.controller;

import com.desafio.revendaservice.application.service.PedidoService;
import com.desafio.revendaservice.domain.model.PedidoPendente;
import com.desafio.revendaservice.infrastructure.repository.PedidoPendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos-pendentes")
public class PedidoPendenteController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoPendenteController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/reprocessar")
    public ResponseEntity<List<PedidoPendente>> reprocessarPendentes() {
        List<PedidoPendente> processados = pedidoService.reprocessarPedidosPendentes();
        return ResponseEntity.ok(processados);
    }
}