package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDTO(
    @Schema(description = "ID do usuário")
    Long id,
    @Schema(description = "Nome do usuário")
    String nome,
    @Schema(description = "E-mail do usuário")
    String email,
    @Schema(description = "Receita do usuário")
    BigDecimal receita,
    @Schema(description = "Usuário verificado")
    Boolean verificado,
    @Schema(description = "Data de criação do usuário")
    LocalDateTime criadoEm
) {

}