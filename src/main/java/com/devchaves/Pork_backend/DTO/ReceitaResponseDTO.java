package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReceitaResponseDTO(
    @Schema(description = "Valor da receita")
    BigDecimal valor
) {

}
