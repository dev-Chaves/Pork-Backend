package com.devchaves.Pork_backend.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ExpenseMesRequest(
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDateTime dataInicio,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDateTime dataFim
) {
}
