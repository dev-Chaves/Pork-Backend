package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserUpdateDTO(
    @Schema(description = "Receita do usuário")
    BigDecimal receita
) {

}