package com.devchaves.Pork_backend.services;

import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,TokenRepository tokenRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
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

        return new RegisterResponseDTO(user.getNome(), user.getEmail(), token.getToken());
    }

    private boolean isValidEmail(String email){
        if(email == null || email.trim().isEmpty()){
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

}
