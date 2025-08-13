package com.devchaves.Pork_backend.DTO;

public record ErrorResponseDTO(
        int status,
        String error,
        String message

) {
}
