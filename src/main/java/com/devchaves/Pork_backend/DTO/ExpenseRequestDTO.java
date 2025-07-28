package com.devchaves.Pork_backend.DTO;

import com.devchaves.Pork_backend.ENUM.CategoriesENUM;

import java.math.BigDecimal;

public record ExpenseRequestDTO(
    BigDecimal valor,
    String descricao,
    CategoriesENUM categoria
) {

}