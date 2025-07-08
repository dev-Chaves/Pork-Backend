package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;

import com.devchaves.Pork_backend.ENUM.CategoriesENUM;

public record ExpenseResponseDTO(
    Long id,
    Long userId,
    String userName,
    BigDecimal valor,
    String descricao,
    CategoriesENUM categoria
) {

}