package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.UserInfoResponse;
import com.devchaves.Pork_backend.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Cacheable(value = "userCache", key = "#userDetails.username")
    public UserInfoResponse consultarInfo(UserDetails userDetails){
        logger.info("Consultando informações do usuário: {}", userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        return new UserInfoResponse(user.getNome());
    }
}
