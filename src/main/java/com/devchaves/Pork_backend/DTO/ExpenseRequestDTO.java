package com.devchaves.Pork_backend.DTO;

import com.devchaves.Pork_backend.ENUM.CategoriesENUM;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ExpenseRequestDTO(
        @NotNull(message = "O valor não pode ser nulo.")
        @Positive(message = "O valor da despesa deve ser maior que zero.") // Garante que o valor é positivo
        BigDecimal valor,

        @NotBlank(message = "A descrição não pode ser vazia.") // Usa @NotBlank para Strings, que verifica nulo e ""
        String descricao,

        @NotNull(message = "A categoria não pode ser nula.")
        CategoriesENUM categoria
) {

}