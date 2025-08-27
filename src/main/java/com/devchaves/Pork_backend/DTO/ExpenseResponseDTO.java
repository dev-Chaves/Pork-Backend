package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;

import com.devchaves.Pork_backend.ENUM.CategoriasDeGastos;
import com.devchaves.Pork_backend.ENUM.CategoriesENUM;

public record ExpenseResponseDTO(
        Long id,
        BigDecimal valor,
        String descricao,
        CategoriasDeGastos categoria
) {

}