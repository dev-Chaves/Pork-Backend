package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;

public record LoginResponseDTO(
    String token,
    String email,
    String nome,
    BigDecimal receita
) {

}