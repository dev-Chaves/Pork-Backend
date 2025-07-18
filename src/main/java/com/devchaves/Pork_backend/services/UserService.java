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
import com.devchaves.Pork_backend.DTO.ResendEmail;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.entity.VerificationTokenEntity;
import com.devchaves.Pork_backend.repository.TokenRepository;
import com.devchaves.Pork_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final TokenService tokenService;

    private final UtilServices utilServices;    

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final String url = "http://localhost:8080/api/auth/verificar?param=";
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,TokenRepository tokenRepository, MailService mailService, TokenService tokenService, UtilServices utilServices){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.mailService = mailService;
        this.tokenService = tokenService;
        this.utilServices = utilServices;
    }

    @Transactional
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

        String verificar = url + token.getToken();

        EmailDTO email = new EmailDTO(user.getEmail(), "Bem vindo ao Pork ! Verifique sua Conta", "Obrigado por se registrar, agora verifica sua conta no link abaixo: !" + verificar);

        mailService.sendEmailToRegister(email);

        return new RegisterResponseDTO(user.getNome(), user.getEmail(), token.getToken(), user.getReceita());
    }

    public LoginResponseDTO login (LoginRequestDTO dto){

        if(dto.email() == null || dto.email().trim().isEmpty() || dto.senha().trim().isEmpty()){
            throw new IllegalArgumentException("Email ou senha não podem conter valores nulos");
        }
    
        if(!isValidEmail(dto.email())){
            throw new IllegalArgumentException("Email inválido");
        }
    
        UserEntity user =userRepository.findByEmail(dto.email()).orElseThrow(() -> new UsernameNotFoundException("Email inválido"));
            
         if (!user.getVerificado()) {
            throw new IllegalArgumentException("Usuário não verificado, por favor verifique seu usuário");
         }

        if(!passwordEncoder.matches(dto.senha(), user.getSenha())){
            throw new IllegalArgumentException("Senha inválida");
        }

         String token = tokenService.generateToken(user);

        return new LoginResponseDTO(token, user.getEmail(), user.getNome(), user.getReceita());

    }

    public void reenviarVerificacao(ResendEmail dto){

        UserEntity user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o email fornecido."));

        if(user.getVerificado()){
            throw new IllegalStateException("Usuário já verificado.");
        }

        VerificationTokenEntity token = new VerificationTokenEntity();

        token.setUser(user);
        
        tokenRepository.save(token);

        String verificar = url + token.getToken();

        EmailDTO email = new EmailDTO(
            dto.email(),
            "Verificação de Conta - Novo Link Pork",
            "Olá, " + user.getNome() + "!\n\n" +
            "Você solicitou um novo link para verificar sua conta no Pork, pois o anterior expirou ou não foi utilizado a tempo.\n\n" +
            "Para ativar sua comta, basta clicar no link abaixo:" +
            verificar + "\n\n" +
            "Se você não solicitou este email, por favor ignore-o."
        );

        mailService.sendEmailToRegister(email);

    }

    private boolean isValidEmail(String email){
        if(email == null || email.trim().isEmpty()){
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

}
