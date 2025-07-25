package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilServices {

    public UserEntity getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserEntity
        ){
            return (UserEntity) authentication.getPrincipal();
        }
        throw new UsernameNotFoundException("Usuário não encontrado!");
    
    }

}
