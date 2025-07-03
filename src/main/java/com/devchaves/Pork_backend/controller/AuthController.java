package com.devchaves.Pork_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devchaves.Pork_backend.DTO.RegisterRequestDTO;
import com.devchaves.Pork_backend.DTO.RegisterResponseDTO;
import com.devchaves.Pork_backend.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        
        RegisterResponseDTO response = userService.register(dto);

        return ResponseEntity.ok(response);
    }
    

}
