package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MetasResponseDTO(String meta, BigDecimal valor, LocalDate data) {
}
