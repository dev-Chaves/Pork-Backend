package com.devchaves.Pork_backend.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devchaves.Pork_backend.entity.UserEntity;

@Service
public class UtilServices {

    public UserEntity getCurrentUser(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication != null && authentication.getPrincipal() instanceof UserEntity){
                return (UserEntity) authentication.getPrincipal();
            }
        }catch(Exception e){
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
        
        return null;
    }

}
