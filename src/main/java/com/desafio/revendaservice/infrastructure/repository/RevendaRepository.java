package com.desafio.revendaservice.infrastructure.repository;

import com.desafio.revendaservice.domain.model.Revenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevendaRepository extends JpaRepository<Revenda, Long> {}