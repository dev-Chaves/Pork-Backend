package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.entity.UserEntity;

import com.devchaves.Pork_backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilServices {

    private final UserRepository userRepository;

    public UtilServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        if(authentication != null
//                && authentication.isAuthenticated()
//                && authentication.getPrincipal() instanceof UserEntity
//        ){
//            return (UserEntity) authentication.getPrincipal();
//        }
//        throw new UsernameNotFoundException("Usuário não encontrado!");

        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    
    }

    public Long getCurrentUserId() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findIdByEmail(email);

    }

    public String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        
        if ((scheme.equals("http") && serverPort != 80) || 
            (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        
        return url.toString();
    }

}
