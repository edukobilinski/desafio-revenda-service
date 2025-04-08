package com.desafio.revendaservice.application.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record RevendaDto(
    @NotBlank String cnpj,
    @NotBlank String razaoSocial,
    @NotBlank String nomeFantasia,
    @Email @NotBlank String email,
    List<@Pattern(regexp = "\\d{10,11}") String> telefones,
    @NotEmpty List<ContatoDto> contatos,
    @NotEmpty List<EnderecoDto> enderecos
) {}