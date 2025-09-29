package com.devchaves.Pork_backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${pork.monitoring.api-key}")
    private String secretApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestApiKey = request.getHeader("X-API-KEY");



        if (secretApiKey.equals(requestApiKey)){
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    "AGENTE_USER",
                    null,
                    AuthorityUtils.createAuthorityList("ROLE_AGENT")
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }
}
