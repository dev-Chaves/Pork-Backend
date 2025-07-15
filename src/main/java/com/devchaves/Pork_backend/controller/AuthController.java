package com.devchaves.Pork_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devchaves.Pork_backend.DTO.LoginRequestDTO;
import com.devchaves.Pork_backend.DTO.LoginResponseDTO;
import com.devchaves.Pork_backend.DTO.RegisterRequestDTO;
import com.devchaves.Pork_backend.DTO.RegisterResponseDTO;
import com.devchaves.Pork_backend.DTO.ResendEmail;
import com.devchaves.Pork_backend.services.TokenService;
import com.devchaves.Pork_backend.services.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    private final TokenService tokenService;

    public AuthController(UserService userService, TokenService tokenService){
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> getMethodName(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(userService.login(dto));
    }
    

    @PostMapping("register")
    @Transactional
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        
        RegisterResponseDTO response = userService.register(dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("verificar")
    public ResponseEntity<String> verificarUsuario(@RequestParam String param) {

        try{
            tokenService.verificarToken(param);

            return ResponseEntity.ok("Verificado!");

        }catch (IllegalArgumentException e){

            return ResponseEntity.badRequest().body("Token inválido!");

        }catch(Exception e){
            
            return ResponseEntity.badRequest().body("Token inválido ou expirado!");

        }
        
    }
    
    @PostMapping("reenviar-email")
    public ResponseEntity<String> reenviarEmail(@Valid @RequestBody ResendEmail dto) {
        userService.reenviarVerificacao(dto);
        return ResponseEntity.ok("Verificando...");
    }
    
}
