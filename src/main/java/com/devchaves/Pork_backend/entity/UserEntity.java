package com.devchaves.Pork_backend.entity;

import com.devchaves.Pork_backend.ENUM.InvestimentoENUM;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "tb_usuarios")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "receita", nullable = false, precision = 15, scale = 2)
    private BigDecimal receita;

    @Column(name = "verificado", nullable = false)
    private Boolean verificado;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseEntity> expenses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerificationTokenEntity> tokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetasEntity> metas;

    @Enumerated(EnumType.STRING)
    private InvestimentoENUM investimento;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
        this.verificado = false;
    }

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public BigDecimal getReceita() {
        return receita;
    }

    public void setReceita(BigDecimal receita) {
        this.receita = receita;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public List<ExpenseEntity> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseEntity> expenses) {
        this.expenses = expenses;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.verificado;
    }

    public List<VerificationTokenEntity> getTokens() {
        return tokens;
    }

    public void setTokens(List<VerificationTokenEntity> tokens) {
        this.tokens = tokens;
    }

    

}
