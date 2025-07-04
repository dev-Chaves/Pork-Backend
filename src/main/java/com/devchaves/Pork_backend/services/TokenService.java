package com.devchaves.Pork_backend.services;

import org.springframework.stereotype.Service;

import com.devchaves.Pork_backend.repository.TokenRepository;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(){
        
        return "GeneratedToken";
    }

}
