package com.devchaves.Pork_backend.DTO;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public record ExpenseListDTO(
        @Schema(description = "Lista de despesas")
        List<ExpenseResponseDTO> list
) {
}
