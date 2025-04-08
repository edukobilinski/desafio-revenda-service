package com.desafio.revendaservice.infrastructure.repository;

import com.desafio.revendaservice.domain.model.PedidoPendente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoPendenteRepository extends JpaRepository<PedidoPendente, Long> {}