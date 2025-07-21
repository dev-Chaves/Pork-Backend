package com.devchaves.Pork_backend.DTO;

import com.devchaves.Pork_backend.ENUM.InvestimentoENUM;

import java.math.BigDecimal;

public record InvestmentMethodsResponse(
        InvestimentoENUM categoria,
        BigDecimal valor
) {
}
