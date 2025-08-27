package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;

import com.devchaves.Pork_backend.ENUM.CategoriasDeGastos;
import com.devchaves.Pork_backend.ENUM.CategoriesENUM;
import io.swagger.v3.oas.annotations.media.Schema;

public record ExpenseResponseDTO(
        @Schema(description = "ID da despesa", example = "1")
        Long id,

        @Schema(description = "Valor da despesa", example = "150.75")
        BigDecimal valor,

        @Schema(description = "Descrição da despesa", example = "Supermercado")
        String descricao,

        @Schema(description = "Categoria da despesa", example = "ALIMENTACAO")
        CategoriasDeGastos categoria
) {

}