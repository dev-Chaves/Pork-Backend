package com.devchaves.Pork_backend.DTO;

import java.math.BigDecimal;
import java.util.List;

public record DashboardDTO(

    List<ExpenseResponseDTO> todasDespesas,
    List<ExpenseResponseDTO> variaveis,
    List<ExpenseResponseDTO> fixas,
    BigDecimal totalDespesas

) {

}
