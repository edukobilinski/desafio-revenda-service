package com.desafio.revendaservice.api.controller;

import com.desafio.revendaservice.application.dto.PedidoClienteDto;
import com.desafio.revendaservice.application.dto.RevendaDto;
import com.desafio.revendaservice.application.service.RevendaService;
import com.desafio.revendaservice.domain.model.PedidoRevenda;
import com.desafio.revendaservice.domain.model.Revenda;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/revendas")
public class RevendaController {

    private final RevendaService revendaService;

    @Autowired
    public RevendaController(RevendaService revendaService) {
        this.revendaService = revendaService;
    }

    @PostMapping
    public ResponseEntity<Revenda> cadastrar(@RequestBody @Valid RevendaDto dto) {
        return ResponseEntity.ok(revendaService.cadastrarRevenda(dto));
    }

    @PostMapping("/{id}/pedidos")
    public ResponseEntity<PedidoRevenda> receberPedido(@PathVariable Long id, @RequestBody @Valid PedidoClienteDto dto) {
        return ResponseEntity.ok(revendaService.receberPedido(id, dto));
    }
}