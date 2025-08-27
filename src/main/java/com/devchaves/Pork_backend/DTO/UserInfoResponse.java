package com.devchaves.Pork_backend.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserInfoResponse(

        @Schema(description = "Nome do usuário")
        String nome

) {
}
