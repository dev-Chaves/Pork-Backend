package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;

public record RegisterResponseDTO(
    String nome, 
    String email, 
    String token,
    BigDecimal receita
) {

}
