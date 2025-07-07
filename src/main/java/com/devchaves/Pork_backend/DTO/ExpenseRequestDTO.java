package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;
import com.devchaves.Pork_backend.ENUM.CategoriesENUM;

public record ExpenseRequestDTO(
    BigDecimal receita,
    BigDecimal valor,
    String descricao,
    CategoriesENUM categoria
) {

}