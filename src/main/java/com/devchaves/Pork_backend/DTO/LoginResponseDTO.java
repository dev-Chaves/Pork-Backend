package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(
    @Schema(description = "Token JWT de autenticação")
    String token,
    @Schema(description = "E-mail do usuário")
    String email,
    @Schema(description = "Nome do usuário")
    String nome,
    @Schema(description = "Receita do usuário")
    BigDecimal receita
) {

}