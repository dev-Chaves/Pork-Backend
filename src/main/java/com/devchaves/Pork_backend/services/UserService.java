package com.devchaves.Pork_backend.services;

import java.util.regex.Pattern;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devchaves.Pork_backend.DTO.EmailDTO;
import com.devchaves.Pork_backend.DTO.LoginRequestDTO;
import com.devchaves.Pork_backend.DTO.LoginResponseDTO;
import com.devchaves.Pork_backend.DTO.RegisterRequestDTO;
import com.devchaves.Pork_backend.DTO.RegisterResponseDTO;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.entity.VerificationTokenEntity;
import com.devchaves.Pork_backend.repository.TokenRepository;
import com.devchaves.Pork_backend.repository.UserRepository;

@Service
public class UserService {

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final TokenService tokenService;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,TokenRepository tokenRepository, MailService mailService, TokenService tokenService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.mailService = mailService;
        this.tokenService = tokenService;
    }

    public RegisterResponseDTO register (RegisterRequestDTO dto){
        

        if(!isValidEmail(dto.email())){
            throw new IllegalArgumentException("Email com formato inválido");
        }

        if(userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        VerificationTokenEntity token = new VerificationTokenEntity();

        UserEntity user = new UserEntity();

        user.setEmail(dto.email());
        user.setNome(dto.nome());
        user.setSenha(passwordEncoder.encode(dto.senha()));

        userRepository.save(user);

        token.setUser(user);

        tokenRepository.save(token);

        String url = "http://localhost:8080/api/auth/verify?param=" + token.getToken();

        EmailDTO email = new EmailDTO(user.getEmail(), "Bem vindo ao Pork ! Verifique sua Conta", "Obrigado por se registrar, agora verifica sua conta no link abaixo: !" + url);

        mailService.sendEmailToRegister(email);

        return new RegisterResponseDTO(user.getNome(), user.getEmail(), token.getToken());
    }

    public LoginResponseDTO login (LoginRequestDTO dto){

        try{
            
            if(dto.email() == null || dto.email().trim().isEmpty() || dto.senha().trim().isEmpty() || dto.senha() == null){
                throw new IllegalArgumentException("Email ou senha não podem conter valores nulos");
            }
    
            if(!isValidEmail(dto.email())){
                throw new IllegalArgumentException("Email inválido");
            }
    
            UserEntity user =userRepository.findByEmail(dto.email()).orElseThrow(() -> new UsernameNotFoundException("Email inválido"));
            
            if (user.getVerificado() == false) {
                throw new IllegalArgumentException("Usuário não verificado, por favor verifique seu usuário");
            }

            if(!passwordEncoder.matches(dto.senha(), user.getSenha())){
                throw new IllegalArgumentException("Senha inválida");
            }
    
            String token = tokenService.generateToken(user);
    
            LoginResponseDTO response = new LoginResponseDTO(token, user.getEmail());
    
            return response;  

        }catch(Exception e){
            throw new RuntimeException("Ocorreu um erro durante o login: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email){
        if(email == null || email.trim().isEmpty()){
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

}
