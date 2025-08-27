package com.devchaves.Pork_backend.DTO;

import com.devchaves.Pork_backend.ENUM.InvestimentoENUM;
import io.swagger.v3.oas.annotations.media.Schema;

public record InvestmentRequestDTO(
    @Schema(description = "Tipo de investimento")
    InvestimentoENUM tipo
) {
}
