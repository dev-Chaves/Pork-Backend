package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.*;
import com.devchaves.Pork_backend.entity.PasswordTokenEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.entity.VerificationTokenEntity;
import com.devchaves.Pork_backend.repository.PasswordTokenRepository;
import com.devchaves.Pork_backend.repository.UserRepository;
import com.devchaves.Pork_backend.repository.VerificationTokenRepository;
import com.devchaves.Pork_backend.services.kafka.KafkaProducerService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final TokenService tokenService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final KafkaProducerService kafkaProducerService;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService, TokenService tokenService, VerificationTokenRepository verificationTokenRepository, PasswordTokenRepository passwordTokenRepository, KafkaProducerService kafkaProducerService, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.tokenService = tokenService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.userService = userService;
    }

    @Value("${url.redefinir-senha}")
    private String redefinirSenhaUrl;

    @Value("${url.verificar-conta}")
    private String verificarContaUrl;

    @Transactional
    public CompletableFuture<Void> register (RegisterRequestDTO dto, String url){
        logger.info("Iniciando processo de registro para o email: {}", dto.email());
        if(!isValidEmail(dto.email())){
            logger.warn("Tentativa de registro com email inválido: {}", dto.email());
            throw new IllegalArgumentException("Email com formato inválido");
        }

        if(userRepository.existsByEmail(dto.email())) {
            logger.warn("Tentativa de registro com email já existente: {}", dto.email());
            throw new IllegalArgumentException("Email já está em uso");
        }

        String passwordEncrypted = passwordEncoder.encode(dto.senha());

        UserEntity user = UserEntity.from(dto, passwordEncrypted);

        VerificationTokenEntity token = VerificationTokenEntity.from(user);

        userRepository.save(user);

        logger.info("Usuário salvo no banco de dados com o email: {}", dto.email());

        token.alterarUsuario(user);

        verificationTokenRepository.save(token);

        logger.info("Token de verificação gerado para o email: {}", dto.email());

        String param = "?token=";

        String verificar = verificarContaUrl + param + token.getToken();

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

        kafkaProducerService.sendEmailEvent(email);

        logger.info("Enviando email de verificação para: {}", dto.email());

        return CompletableFuture.completedFuture(null);
    }

    public LoginResponseDTO login (LoginRequestDTO dto){
        logger.info("Tentativa de login para o email: {}", dto.email());
        if(dto.email() == null || dto.email().trim().isEmpty() || dto.senha().trim().isEmpty()){
            logger.warn("Tentativa de login com email ou senha nulos.");
            throw new IllegalArgumentException("Email ou senha não podem conter valores nulos");
        }

        if(!isValidEmail(dto.email())){
            logger.warn("Tentativa de login com email em formato inválido: {}", dto.email());
            throw new IllegalArgumentException("Email inválido");
        }

        UserEntity user = userRepository.findByEmail(dto.email()).orElseThrow(() -> {
            logger.warn("Tentativa de login com email não cadastrado: {}", dto.email());
            return new UsernameNotFoundException("Email inválido");
        });

        if (!user.getVerificado()) {
            logger.warn("Tentativa de login de usuário não verificado: {}", dto.email());
            throw new IllegalArgumentException("Usuário não verificado, por favor verifique seu usuário");
        }

        if(!passwordEncoder.matches(dto.senha(), user.getSenha())){
            logger.warn("Tentativa de login com senha inválida para o email: {}", dto.email());
            throw new IllegalArgumentException("Senha inválida");
        }

        String token = tokenService.generateToken(user);

        userService.consultarInfo(user);

        logger.info("Login bem-sucedido e token gerado para o email: {}", dto.email());

        return new LoginResponseDTO(token, user.getEmail(), user.getNome(), user.getReceita());

    }

    public void reenviarVerificacao(ResendEmail dto, String url){
        logger.info("Solicitação de reenvio de verificação para o email: {}", dto.email());
        UserEntity user = userRepository.findByEmail(dto.email()).orElseThrow(() -> {
            logger.warn("Tentativa de reenvio de verificação para email não encontrado: {}", dto.email());
            return new IllegalArgumentException("Usuário não encontrado com o email fornecido.");
        });

        if(user.getVerificado()){
            logger.warn("Tentativa de reenvio de verificação para usuário já verificado: {}", dto.email());
            throw new IllegalStateException("Usuário já verificado.");
        }

        VerificationTokenEntity token = VerificationTokenEntity.from(user);

        token.alterarUsuario(user);

        verificationTokenRepository.save(token);

        logger.info("Novo token de verificação gerado para: {}", dto.email());

        String urlTeste = "https://financepork.site/verificar-email?token=";
        String verificar = urlTeste + token.getToken();

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
        logger.info("Enviando novo email de verificação para: {}", dto.email());

        kafkaProducerService.sendEmailEvent(email);

    }

    public void enviarEmaiLRedefenirSenha(String email){
        logger.info("Solicitação de redefinição de senha para o email: {}", email);
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> {
            logger.warn("Tentativa de redefinição de senha para email não encontrado: {}", email);
            return new UsernameNotFoundException("Usuário não encontrado");
        });

        PasswordTokenEntity token = PasswordTokenEntity.from(user);

        passwordTokenRepository.save(token);

        logger.info("Token de redefinição de senha gerado para: {}", email);

        String url = redefinirSenhaUrl + "?token=" + token.getToken();

        String corpoEmail = String.format(
                "Olá %s,\n\n" +
                        "Você solicitou a redefinição da sua senha. Clique no link abaixo para continuar:\n\n" +
                        "🔗 %s\n\n" +
                        "Se você não fez esta solicitação, pode ignorar este e-mail.",
                user.getNome(),
                url
        );

        EmailDTO emailDto = new EmailDTO(
                user.getEmail(),
                "Pork - Redefinição de Senha",
                corpoEmail
        );

        logger.info("Enviando email de redefinição de senha para: {}", email);

        kafkaProducerService.sendEmailEvent(emailDto);
    }

    @Transactional
    public void redefinirSenha(ChangePasswordRequest dto, String token){
        logger.info("Tentativa de redefinição de senha com token.");
        PasswordTokenEntity resetToken = passwordTokenRepository.findByToken(token).orElseThrow(() -> {
            logger.warn("Tentativa de redefinição de senha com token inválido.");
            return new IllegalArgumentException("Token inválido");
        });

        UserEntity user = resetToken.getUser();
        logger.info("Redefinindo senha para o usuário: {}", user.getEmail());

        if(!Objects.equals(dto.password(), dto.secondPassword())){
            logger.warn("Tentativa de redefinição com senhas que não coincidem para o usuário: {}", user.getEmail());
            throw new IllegalArgumentException("Senhas devem ser iguais");
        }

        user.atualizarSenha(passwordEncoder.encode(dto.password()));

        userRepository.save(user);

        logger.info("Senha redefinida com sucesso para o usuário: {}", user.getEmail());

        passwordTokenRepository.delete(resetToken);

        logger.info("Token de redefinição de senha excluído.");
    }

    private boolean isValidEmail(String email){
        if(email == null || email.trim().isEmpty()){
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

}
