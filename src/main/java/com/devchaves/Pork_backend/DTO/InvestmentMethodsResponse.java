package com.devchaves.Pork_backend.DTO;

import com.devchaves.Pork_backend.ENUM.InvestimentoENUM;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record InvestmentMethodsResponse(
        @Schema(description = "Categoria do investimento")
        InvestimentoENUM categoria,
        @Schema(description = "Valor do investimento")
        BigDecimal valor
) {
}
