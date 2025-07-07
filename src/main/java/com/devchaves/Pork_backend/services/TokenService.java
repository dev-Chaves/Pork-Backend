package com.devchaves.Pork_backend.services;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.entity.VerificationTokenEntity;
import com.devchaves.Pork_backend.repository.TokenRepository;
import com.devchaves.Pork_backend.repository.UserRepository;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    private final long expirationTime = 86400000;

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    public TokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public void verificarToken(String request) {

        if (request == null || request.isEmpty()) {
            throw new IllegalArgumentException("Token não pode ser inválido ou vazio!");
        }

        VerificationTokenEntity token = tokenRepository.findByToken(request);

        verificarExpiracao(token);

        UserEntity user = token.getUser();

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        user.setVerificado(true);

        userRepository.save(user);

        tokenRepository.delete(token);

    }

    public static void verificarExpiracao(VerificationTokenEntity token) {

        LocalDateTime now = LocalDateTime.now();

        if (token.getExpira_em().isBefore(now)) {
            throw new IllegalArgumentException("Token expirado!");
        }

    }

    public String generateToken(UserEntity user){

        try{
            if(user == null){
                throw new IllegalArgumentException("Usuário não pode ser nulo!");
            }

            Algorithm algorithm = Algorithm.HMAC256(secret);

            Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

            String token = JWT.create()
                .withSubject(user.getEmail())
                .withIssuer("API PORK")
                .withExpiresAt(expirationDate)
                .sign(algorithm);

                return token;

        }catch(JWTCreationException e){
            throw new RuntimeException("Error ao criar o token", e);
        }
    }

    public String estaValido(String token){

        try {
            if(token == null){
                throw new IllegalArgumentException("Token não pode ser nulo");
            }

            Algorithm algorithm = Algorithm.HMAC256(secret);

            String subject = JWT.require(algorithm)
            .withIssuer("API PORK")
            .build()
            .verify(token)
            .getSubject();

            return subject;
        }catch(JWTVerificationException e){
            throw new JWTVerificationException("Erro a válidar token");
        }
    }

    public Date getDateExpiration(String token) {

        try {
            if(token == null || token.isEmpty()){
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
            throw new RuntimeException("Error getting expiration date: " + e.getMessage());
        }

    }
}
