package com.desafio.revendaservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Endereço de entrega da revenda")
public record EnderecoDto(

    @Schema(description = "Nome da rua", example = "Rua das Bebidas")
    @NotBlank String rua,

    @Schema(description = "Número do endereço", example = "100")
    @NotBlank String numero,

    @Schema(description = "Cidade do endereço", example = "São Paulo")
    @NotBlank String cidade,

    @Schema(description = "Estado (UF)", example = "SP")
    @NotBlank String estado,

    @Schema(description = "CEP do endereço", example = "01000-000")
    @NotBlank String cep
) {}
