package com.devchaves.Pork_backend.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChangePasswordRequest(
        @Schema(description = "Nova senha senha do usuário", example = "novaSenha456")
        String password,
        @Schema(description = "Nova senha do usuário - verificação", example = "novaSenha456")
        String secondPassword
) {
}
