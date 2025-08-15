package com.devchaves.Pork_backend.config;

import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.UserRepository;
import com.devchaves.Pork_backend.services.ExpensesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "userDetailsCache", key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        logger.info("Consultando usuário loadUserByUsername(), deve ser acessado apenas na primeira vez, ou após ser inválidado");

        Long startTime = System.currentTimeMillis();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não foi encontrado: " + email));

        Long endTime = System.currentTimeMillis();

        logger.info("Tempo gasto = " + (endTime - startTime));

        return user;
    }
}
