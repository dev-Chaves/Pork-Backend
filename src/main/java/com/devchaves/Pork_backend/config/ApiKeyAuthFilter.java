package com.devchaves.Pork_backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${pork.monitoring.api-key}")
    private String secretApiKey;

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestApiKey = request.getHeader("X-API-KEY");

        if (secretApiKey.equals(requestApiKey)){

            // Adicione o log aqui!
            logger.info("🤖 Acesso via API Key concedido para o endpoint: {}. IP de origem: {}", request.getRequestURI(), request.getRemoteAddr());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    "AGENT_USER",
                    null,
                    AuthorityUtils.createAuthorityList("ROLE_AGENT")
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
