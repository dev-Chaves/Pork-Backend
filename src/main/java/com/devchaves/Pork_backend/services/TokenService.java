package com.devchaves.Pork_backend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.entity.VerificationTokenEntity;
import com.devchaves.Pork_backend.repository.UserRepository;
import com.devchaves.Pork_backend.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    private final long expirationTime = 86400000;
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    public TokenService( VerificationTokenRepository verificationTokenRepository, UserRepository userRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
    }

    public void verificarToken(String request) {
        logger.info("Iniciando verificação de token de email.");
        if (request == null || request.isEmpty()) {
            logger.warn("Tentativa de verificação com token nulo ou vazio.");
            throw new IllegalArgumentException("Token não pode ser inválido ou vazio!");
        }

        VerificationTokenEntity token = verificationTokenRepository.findByToken(request).orElseThrow(() -> {
            logger.warn("Token de verificação não encontrado no banco de dados.");
            return new IllegalArgumentException("Token de verificação inválido");
        });

        verificarExpiracao(token);

        UserEntity user = token.getUser();
        if (user == null) {
            logger.error("Usuário associado ao token de verificação não foi encontrado.");
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        user.verificarUsuario(true);

        userRepository.save(user);

        logger.info("Usuário {} verificado com sucesso.", user.getEmail());

        verificationTokenRepository.delete(token);
        logger.info("Token de verificação removido após o uso.");
    }

    public static void verificarExpiracao(VerificationTokenEntity token) {
        LocalDateTime now = LocalDateTime.now();
        if (token.getExpira_em().isBefore(now)) {
            logger.warn("Tentativa de usar um token de verificação expirado para o usuário: {}", token.getUser().getEmail());
            throw new IllegalArgumentException("Token expirado!");
        }
    }

    public String generateToken(UserEntity user){
        try{
            if(user == null){
                logger.error("Tentativa de gerar token para usuário nulo.");
                throw new IllegalArgumentException("Usuário não pode ser nulo!");
            }
            logger.info("Gerando token JWT para o usuário: {}", user.getEmail());
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

            String token = JWT.create()
                .withSubject(user.getEmail())
                .withIssuer("API PORK")
                .withExpiresAt(expirationDate)
                .sign(algorithm);
            
            logger.info("Token JWT gerado com sucesso para o usuário: {}", user.getEmail());
            return token;
        }catch(JWTCreationException e){
            logger.error("Erro ao criar o token JWT para o usuário: {}", user.getEmail(), e);
            throw new RuntimeException("Error ao criar o token", e);
        }
    }

    public String estaValido(String token){
        try {
            if(token == null){
                logger.warn("Tentativa de validar um token nulo.");
                throw new IllegalArgumentException("Token não pode ser nulo");
            }
            logger.debug("Validando token JWT.");
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String subject = JWT.require(algorithm)
                .withIssuer("API PORK")
                .build()
                .verify(token)
                .getSubject();
            logger.debug("Token JWT validado com sucesso para o subject: {}", subject);
            return subject;
        }catch(JWTVerificationException e){
            logger.warn("Falha na validação do token JWT.", e);
            throw new JWTVerificationException("Erro a válidar token");
        }
    }

    public Date getDateExpiration(String token) {
        try {
            if(token == null || token.isEmpty()){
                logger.warn("Tentativa de obter data de expiração de um token nulo ou vazio.");
                return null;
            }
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Date expirationDate = JWT.require(algorithm)
                    .withIssuer("API PORK")
                    .build()
                    .verify(token)
                    .getExpiresAt();
            return expirationDate;
        } catch (Exception e){
            logger.error("Erro ao obter a data de expiração do token.", e);
            throw new RuntimeException("Error getting expiration date: " + e.getMessage());
        }
    }
}
