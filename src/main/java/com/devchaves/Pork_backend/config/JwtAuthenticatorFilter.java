package com.devchaves.Pork_backend.config;

import com.devchaves.Pork_backend.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticatorFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final CustomUserDetailsService customUserDetailsService;

    private static  final Logger logger = LoggerFactory.getLogger(JwtAuthenticatorFilter.class);

    public JwtAuthenticatorFilter(TokenService tokenService, CustomUserDetailsService customUserDetailsService) {
        this.tokenService = tokenService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        if (request.getCookies() != null) {
           for(jakarta.servlet.http.Cookie cookie : request.getCookies()){
               if("jwt".equals(cookie.getName())){
                   token = cookie.getValue();
                   break;
               }
           }
        }

        if(token != null){

            try{
                String username = tokenService.estaValido(token);

                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }catch(Exception e){
                logger.error("Erro ao validar o token JWT: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT Token inválido + ");
                return;
            }
        }

        filterChain.doFilter(request, response);
            
    }

}
