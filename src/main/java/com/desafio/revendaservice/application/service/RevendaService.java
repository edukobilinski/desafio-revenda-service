package com.desafio.revendaservice.application.service;

import com.desafio.revendaservice.application.dto.PedidoClienteDto;
import com.desafio.revendaservice.application.dto.RevendaDto;
import com.desafio.revendaservice.domain.model.Contato;
import com.desafio.revendaservice.domain.model.Endereco;
import com.desafio.revendaservice.domain.model.ItemPedido;
import com.desafio.revendaservice.domain.model.PedidoRevenda;
import com.desafio.revendaservice.domain.model.Revenda;
import com.desafio.revendaservice.infrastructure.repository.PedidoRevendaRepository;
import com.desafio.revendaservice.infrastructure.repository.RevendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RevendaService {

    private final RevendaRepository revendaRepository;
    private final PedidoRevendaRepository pedidoRepository;

    @Autowired
    public RevendaService(RevendaRepository revendaRepository, PedidoRevendaRepository pedidoRepository) {
        this.revendaRepository = revendaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public Revenda cadastrarRevenda(RevendaDto dto) {
        Revenda revenda = Revenda.builder()
            .cnpj(dto.cnpj())
            .razaoSocial(dto.razaoSocial())
            .nomeFantasia(dto.nomeFantasia())
            .email(dto.email())
            .telefones(dto.telefones())
            .contatos(dto.contatos().stream()
                .map(c -> Contato.builder().nome(c.nome()).principal(c.principal()).build())
                .collect(Collectors.toList()))
            .enderecos(dto.enderecos().stream()
                .map(e -> Endereco.builder()
                    .rua(e.rua()).numero(e.numero())
                    .cidade(e.cidade()).estado(e.estado()).cep(e.cep())
                    .build())
                .collect(Collectors.toList()))
            .build();

        return revendaRepository.save(revenda);
    }

    public PedidoRevenda receberPedido(Long revendaId, PedidoClienteDto dto) {
        PedidoRevenda pedido = PedidoRevenda.builder()
            .revendaId(revendaId)
            .clienteId(dto.clienteId())
            .itens(dto.itens().stream()
                .map(i -> ItemPedido.builder().produto(i.produto()).quantidade(i.quantidade()).build())
                .collect(Collectors.toList()))
            .enviado(false)
            .build();

        return pedidoRepository.save(pedido);
    }
}