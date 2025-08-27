package com.devchaves.Pork_backend.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record InvestmentResponseDTO(
    @Schema(description = "Tipo de investimento")
    String tipo
) {
}
