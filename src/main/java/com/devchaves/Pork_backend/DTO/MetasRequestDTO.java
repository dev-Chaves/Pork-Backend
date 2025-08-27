package com.devchaves.Pork_backend.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MetasRequestDTO (
        @Schema(description = "Nome da meta")
        String meta,
        @Schema(description = "Valor da meta")
        BigDecimal valor,
        @Schema(description = "Data da meta")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data) {
}
