package com.devchaves.Pork_backend.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ExpenseMesRequest(
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dataInicio,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dataFim
) {
}
