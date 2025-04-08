package com.desafio.revendaservice.application.service;

import com.desafio.revendaservice.application.dto.*;
import com.desafio.revendaservice.api.exception.RevendaNotFoundException;
import com.desafio.revendaservice.domain.model.*;
import com.desafio.revendaservice.infrastructure.repository.PedidoRevendaRepository;
import com.desafio.revendaservice.infrastructure.repository.RevendaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RevendaService {

    private static final Logger log = LoggerFactory.getLogger(RevendaService.class);

    private final RevendaRepository revendaRepository;
    private final PedidoRevendaRepository pedidoRepository;
    private final PedidoService pedidoService;

    @Autowired
    public RevendaService(RevendaRepository revendaRepository,
                          PedidoRevendaRepository pedidoRepository,
                          PedidoService pedidoService) {
        this.revendaRepository = revendaRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoService = pedidoService;
    }

    public Revenda cadastrarRevenda(RevendaDto dto) {
        log.info("Iniciando cadastro de nova revenda: {}", dto.cnpj());

        Revenda revenda = Revenda.builder()
            .cnpj(dto.cnpj())
            .razaoSocial(dto.razaoSocial())
            .nomeFantasia(dto.nomeFantasia())
            .email(dto.email())
            .telefones(dto.telefones())
            .contatos(dto.contatos().stream().map(this::toContato).collect(Collectors.toList()))
            .enderecos(dto.enderecos().stream().map(this::toEndereco).collect(Collectors.toList()))
            .build();

        Revenda salva = revendaRepository.save(revenda);
        log.info("Revenda cadastrada com sucesso. ID: {}", salva.getId());
        return salva;
    }

    public PedidoRevenda receberPedido(Long revendaId, PedidoClienteDto dto) {
        log.info("Recebendo pedido do cliente {} para revenda {}", dto.clienteId(), revendaId);

        validarRevendaExistente(revendaId);

        PedidoRevenda pedido = PedidoRevenda.builder()
            .revendaId(revendaId)
            .clienteId(dto.clienteId())
            .itens(dto.itens().stream().map(this::toItemPedido).collect(Collectors.toList()))
            .enviado(false)
            .build();

        pedidoService.emitirPedido(pedido);

        PedidoRevenda salvo = pedidoRepository.save(pedido);
        log.info("Pedido salvo com sucesso para revenda {}. ID: {}", revendaId, salvo.getId());
        return salvo;
    }

    public List<Revenda> buscarTodas() {
        log.info("Buscando todas as revendas cadastradas");
        return revendaRepository.findAll();
    }

    public Revenda buscarPorId(Long id) {
        log.info("Buscando revenda por ID: {}", id);
        return revendaRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Revenda com id {} não encontrada", id);
                return new RevendaNotFoundException("Revenda com id " + id + " não encontrada");
            });
    }

    public List<PedidoRevenda> listarPedidosPorRevenda(Long revendaId) {
        log.info("Listando pedidos para revenda ID={}", revendaId);
        validarRevendaExistente(revendaId);

        List<PedidoRevenda> pedidos = pedidoRepository.findByRevendaId(revendaId);
        log.info("Total de pedidos encontrados para revenda ID={}: {}", revendaId, pedidos.size());

        return pedidos;
    }

    private void validarRevendaExistente(Long revendaId) {
        log.debug("Validando existência da revenda: {}", revendaId);
        if (!revendaRepository.existsById(revendaId)) {
            log.warn("Revenda com id {} não existe", revendaId);
            throw new RevendaNotFoundException("Revenda com id " + revendaId + " não existe.");
        }
    }

    private Contato toContato(ContatoDto dto) {
        return Contato.builder()
            .nome(dto.nome())
            .principal(dto.principal())
            .build();
    }

    private Endereco toEndereco(EnderecoDto dto) {
        return Endereco.builder()
            .rua(dto.rua())
            .numero(dto.numero())
            .cidade(dto.cidade())
            .estado(dto.estado())
            .cep(dto.cep())
            .build();
    }

    private ItemPedido toItemPedido(ItemPedidoDto dto) {
        return ItemPedido.builder()
            .produto(dto.produto())
            .quantidade(dto.quantidade())
            .build();
    }
}
