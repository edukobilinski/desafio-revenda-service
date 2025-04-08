package com.desafio.revendaservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Pessoa de contato da revenda")
public record ContatoDto(

    @Schema(description = "Nome do contato", example = "João da Silva")
    @NotBlank String nome,

    @Schema(description = "Define se é o contato principal", example = "true")
    boolean principal
) {}
