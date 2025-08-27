package com.devchaves.Pork_backend.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterRequestDTO(
    @Schema(description = "Nome do usuário")
    String nome,
    @Schema(description = "E-mail do usuário")
    String email,
    @Schema(description = "Senha do usuário")
    String senha
) {

}
