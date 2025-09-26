package com.devchaves.Pork_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_password_token")
public class PasswordTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "expira_em", nullable = false)
    private LocalDateTime expiraEm;

    @Column(nullable = false)
    private Boolean expirado;

    @PrePersist()
    protected void onCreate() {
        this.token = UUID.randomUUID().toString();
        this.criadoEm = LocalDateTime.now();
        this.expirado = false;
        this.expiraEm = LocalDateTime.now().plusMinutes(10);
    }

    private PasswordTokenEntity() {
    }

    public static PasswordTokenEntity from (UserEntity user){
        PasswordTokenEntity token = new PasswordTokenEntity();
        token.setUser(user);
        return token;
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    private void setUser(UserEntity user) {
        this.user = user;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    private void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getExpiraEm() {
        return expiraEm;
    }

    private void setExpiraEm(LocalDateTime expiraEm) {
        this.expiraEm = expiraEm;
    }

    public Boolean getExpirado() {
        return expirado;
    }

    private void setExpirado(Boolean expirado) {
        this.expirado = expirado;
    }
}
