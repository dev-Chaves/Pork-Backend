package com.devchaves.Pork_backend.controller;

import com.devchaves.Pork_backend.DTO.*;
import com.devchaves.Pork_backend.services.TokenService;
import com.devchaves.Pork_backend.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<LoginResponseDTOV2> login(@Valid @RequestBody LoginRequestDTO dto, HttpServletResponse response) {

        LoginResponseDTO loginResponse = userService.login(dto);

        LoginResponseDTOV2 loginResponseDTOV2 = new LoginResponseDTOV2(loginResponse.email(), loginResponse.receita());

        String token = loginResponse.token();

        String cookieValue = "jwt=" + token +
                "; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=" + (24 * 60 * 60);

        response.setHeader("Set-Cookie", cookieValue);

        return ResponseEntity.ok(loginResponseDTOV2);
    }
    

    @PostMapping("register")
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
