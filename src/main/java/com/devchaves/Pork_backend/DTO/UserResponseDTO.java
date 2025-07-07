package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserResponseDTO(
    Long id,
    String nome,
    String email,
    BigDecimal receita,
    Boolean verificado,
    LocalDateTime criadoEm
) {

}