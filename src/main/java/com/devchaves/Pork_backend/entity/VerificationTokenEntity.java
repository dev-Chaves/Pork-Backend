package com.devchaves.Pork_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_verification_tokens")
public class VerificationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


    @Column(name = "expira_em", nullable = false)
    private LocalDateTime expira_em;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criado_em;

    @Column(name = "expirado", nullable = false)
    private boolean expirado;

    @PrePersist
    public void prePersist() {
        this.token = UUID.randomUUID().toString();
        this.criado_em = LocalDateTime.now();
        this.expira_em = this.criado_em.plusMinutes(10); 
        this.expirado = false;
    }

    public VerificationTokenEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public LocalDateTime getExpira_em() {
        return expira_em;
    }

    public void setExpira_em(LocalDateTime expira_em) {
        this.expira_em = expira_em;
    }

    public LocalDateTime getCriado_em() {
        return criado_em;
    }

    public void setCriado_em(LocalDateTime criado_em) {
        this.criado_em = criado_em;
    }

    
    

}
