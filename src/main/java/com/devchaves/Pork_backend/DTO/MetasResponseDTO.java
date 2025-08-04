package com.devchaves.Pork_backend.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MetasResponseDTO(
        String meta,
        BigDecimal valor,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data) {
}
