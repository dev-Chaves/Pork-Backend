package com.devchaves.Pork_backend.DTO;

public record ChangePasswordRequest(
        String password,
        String secondPassword
) {
}
