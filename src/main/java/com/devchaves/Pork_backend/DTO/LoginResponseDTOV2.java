package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;

public record LoginResponseDTOV2(
        String email,
        BigDecimal receita
) {
}
