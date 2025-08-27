package com.devchaves.Pork_backend.controller;

import com.devchaves.Pork_backend.DTO.*;
import com.devchaves.Pork_backend.services.TokenService;
import com.devchaves.Pork_backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


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

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("None")
                .domain(".financepork.site")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(loginResponseDTOV2);
    }
    

    @PostMapping("register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto, HttpServletRequest request) {

        String baseUrl = getBaseUrl(request) + "/api/auth/verificar-email?param=";
        
        RegisterResponseDTO response = userService.register(dto, baseUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("verificar")
    public ResponseEntity<String> verificarUsuario(@RequestParam("token") String param) {

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
    public ResponseEntity<String> reenviarEmail(@Valid @RequestBody ResendEmail dto, HttpServletRequest request) {

        String baseUrl = getBaseUrl(request) + "/api/auth/verificar-email?param=";
        
        userService.reenviarVerificacao(dto, baseUrl);

        return ResponseEntity.ok("Verificando...");
    }

    @PostMapping("redefinir-email-senha")
    public ResponseEntity<String> reenviarEmailSenha(@Valid @RequestBody ResendEmail dto, HttpServletRequest request){

        userService.enviarEmaiLRedefenirSenha(dto.email());

        return ResponseEntity.ok("Enviado Email!");
    }

    @PostMapping("redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestParam("token") String token, @Valid @RequestBody ChangePasswordRequest dto){

        userService.redefinirSenha(dto, token);

        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .domain(".financepork.site")
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok("Logout efetuado com sucesso!");
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
    
        if ((scheme.equals("http") && serverPort != 80) || 
            (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        
        return url.toString();
    }
    
}
