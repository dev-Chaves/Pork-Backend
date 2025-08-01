package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.*;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.entity.VerificationTokenEntity;
import com.devchaves.Pork_backend.repository.UserRepository;
import com.devchaves.Pork_backend.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final TokenService tokenService;

    private final UtilServices utilServices;

    private final VerificationTokenRepository verificationTokenRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // private static final String url = "http://localhost/api/auth/verificar?param=";
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository, MailService mailService, TokenService tokenService, UtilServices utilServices){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.tokenService = tokenService;
        this.utilServices = utilServices;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Transactional
    public RegisterResponseDTO register (RegisterRequestDTO dto, String url){
        
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

        verificationTokenRepository.save(token);

        String verificar = url + token.getToken();

        String emailBody = String.format(
            "Olá %s!\n\n" +
            "Bem-vindo ao Pork! 🎉\n\n" +
            "Obrigado por se registrar em nossa plataforma. Para começar a usar todos os recursos, " +
            "você precisa verificar sua conta clicando no link abaixo:\n\n" +
            "🔗 %s\n\n" +
            "Este link é válido por 10 minutos. Se você não conseguir clicar no link, " +
            "copie e cole o endereço completo no seu navegador.\n\n" +
            "Se você não se registrou no Pork, pode ignorar este email com segurança.\n\n" +
            "Atenciosamente,\n" +
            "Equipe Pork",
            user.getNome(),
            verificar
        );

        EmailDTO email = new EmailDTO(
            user.getEmail(),
            "✅ Bem-vindo ao Pork - Confirme sua conta",
            emailBody
        );

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
    
        UserEntity user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new UsernameNotFoundException("Email inválido"));
            
         if (!user.getVerificado()) {
            throw new IllegalArgumentException("Usuário não verificado, por favor verifique seu usuário");
         }

        if(!passwordEncoder.matches(dto.senha(), user.getSenha())){
            throw new IllegalArgumentException("Senha inválida");
        }

         String token = tokenService.generateToken(user);

        return new LoginResponseDTO(token, user.getEmail(), user.getNome(), user.getReceita());

    }

    public void reenviarVerificacao(ResendEmail dto, String url){

        UserEntity user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o email fornecido."));

        if(user.getVerificado()){
            throw new IllegalStateException("Usuário já verificado.");
        }

        VerificationTokenEntity token = new VerificationTokenEntity();

        token.setUser(user);
        
        verificationTokenRepository.save(token);

        String verificar = url + token.getToken();

        String emailBody = String.format(
            "Olá %s!\n\n" +
            "Você solicitou um novo link de verificação para sua conta no Pork.\n\n" +
            "Para ativar sua conta, clique no link abaixo:\n\n" +
            "🔗 %s\n\n" +
            "⏰ Este link é válido por 10 minutos a partir do momento deste email.\n\n" +
            "💡 Dica: Se você não conseguir clicar no link, copie e cole o endereço " +
            "completo no seu navegador.\n\n" +
            "Se você não solicitou este email, pode ignorá-lo com segurança. " +
            "Sua conta permanecerá segura.\n\n" +
            "Precisa de ajuda? Entre em contato conosco respondendo este email.\n\n" +
            "Atenciosamente,\n" +
            "Equipe Pork",
            user.getNome(),
            verificar
        );

        EmailDTO email = new EmailDTO(
            dto.email(),
            "🔄 Pork - Novo link de verificação da conta",
            emailBody
        );

        mailService.sendEmailToRegister(email);

    }

    public void enviarEmaiLRedefenirSenha(String email){

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));



    }

    public UserInfoResponse consultarInfo(){

        UserEntity user = utilServices.getCurrentUser();

        return new UserInfoResponse(user.getNome());

    }

    private boolean isValidEmail(String email){
        if(email == null || email.trim().isEmpty()){
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

}
