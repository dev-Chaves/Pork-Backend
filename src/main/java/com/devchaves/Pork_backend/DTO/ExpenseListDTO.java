package com.devchaves.Pork_backend.DTO;

import java.util.List;

public record ExpenseListDTO(
        List<ExpenseResponseDTO> list
) {
}
