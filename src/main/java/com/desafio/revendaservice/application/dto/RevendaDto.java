package com.desafio.revendaservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Dados para cadastro de uma revenda")
public record RevendaDto(

    @Schema(description = "CNPJ da revenda", example = "12.345.678/0001-90")
    @NotBlank String cnpj,

    @Schema(description = "Razão social da revenda", example = "Distribuidora Brasil Ltda")
    @NotBlank String razaoSocial,

    @Schema(description = "Nome fantasia da revenda", example = "Brasil Bebidas")
    @NotBlank String nomeFantasia,

    @Schema(description = "Email de contato", example = "contato@brasilbebidas.com")
    @Email @NotBlank String email,

    @Schema(description = "Telefones da revenda")
    List<String> telefones,

    @Schema(description = "Contatos da revenda")
    @NotNull List<ContatoDto> contatos,

    @Schema(description = "Endereços da revenda")
    @NotNull List<EnderecoDto> enderecos
) {}
