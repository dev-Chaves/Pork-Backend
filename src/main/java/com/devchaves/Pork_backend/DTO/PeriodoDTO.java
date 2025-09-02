package com.devchaves.Pork_backend.DTO;

import java.time.LocalDateTime;

public record PeriodoDTO(
        LocalDateTime inicio,
        LocalDateTime fim
) {
}
