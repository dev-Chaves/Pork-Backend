package com.devchaves.Pork_backend.DTO;

public record RegisterRequestDTO(
    String username,
    String email,
    String password
) {

}
