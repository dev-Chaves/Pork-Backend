package com.devchaves.Pork_backend.DTO;

import com.devchaves.Pork_backend.ENUM.CategoriasDeGastos;
import com.devchaves.Pork_backend.ENUM.CategoriesENUM;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ExpenseRequestDTO(
        @Schema(description = "Valor da despesa", example = "150.75")
        @NotNull(message = "O valor não pode ser nulo.")
        @Positive(message = "O valor da despesa deve ser maior que zero.") // Garante que o valor é positivo
        BigDecimal valor,

        @Schema(description = "Descrição da despesa", example = "Supermercado")
        @NotBlank(message = "A descrição não pode ser vazia.") // Usa @NotBlank para Strings, que verifica nulo e ""
        String descricao,

        @Schema(description = "Categoria da despesa", example = "ALIMENTACAO")
        @NotNull(message = "A categoria não pode ser nula.")
        CategoriasDeGastos categoria
) {

}