package com.devchaves.Pork_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "url")
public class UrlConfig {

    private String link;
    private String redefinirSenha;

    // Getters e Setters necessários para o Spring injetar os valores
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRedefinirSenha() {
        return redefinirSenha;
    }

    public void setRedefinirSenha(String redefinirSenha) {
        this.redefinirSenha = redefinirSenha;
    }
}
