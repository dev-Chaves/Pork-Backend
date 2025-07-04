package com.devchaves.Pork_backend.service;

import org.springframework.stereotype.Service;

import com.devchaves.Pork_backend.repository.UserRepository;

@Service
public class TokenService {

    private final UserRepository userRepository;

    private TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    

}
