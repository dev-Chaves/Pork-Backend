package com.devchaves.Pork_backend.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResendEmail(
    @Schema(description = "E-mail do usuário")
    String email
) {

}
