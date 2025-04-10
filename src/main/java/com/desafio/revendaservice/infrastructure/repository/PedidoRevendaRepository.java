package com.desafio.revendaservice.infrastructure.repository;

import com.desafio.revendaservice.domain.model.PedidoRevenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRevendaRepository extends JpaRepository<PedidoRevenda, Long> {
    List<PedidoRevenda> findByRevendaId(Long revendaId);
}
