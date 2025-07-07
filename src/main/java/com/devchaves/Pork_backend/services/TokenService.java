package com.devchaves.Pork_backend.services;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.entity.VerificationTokenEntity;
import com.devchaves.Pork_backend.repository.TokenRepository;
import com.devchaves.Pork_backend.repository.UserRepository;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    public TokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public void verificarToken(String request){

        if(request == null || request.isEmpty()){
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


    public static void verificarExpiracao(VerificationTokenEntity token){

        LocalDateTime now = LocalDateTime.now();

        if(token.getExpira_em().isBefore(now)){
            throw new IllegalArgumentException("Token expirado!");
        }
        
    }
}
