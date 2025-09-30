package com.devchaves.Pork_backend.entity;

import com.devchaves.Pork_backend.DTO.RegisterRequestDTO;
import com.devchaves.Pork_backend.ENUM.InvestimentoENUM;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseEntity> expenses;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerificationTokenEntity> tokens;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetasEntity> metas;

    @Enumerated(EnumType.STRING)
    private InvestimentoENUM investimento;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
        this.verificado = false;
        this.receita = BigDecimal.valueOf(0);
        this.investimento = InvestimentoENUM.MID;
    }

    protected UserEntity() {
    }

    public static UserEntity from (RegisterRequestDTO dto, String password){
        UserEntity user = new UserEntity();
        user.setNome(dto.nome());
        user.setEmail(dto.email());
        user.setSenha(password);
        user.setVerificado(Boolean.FALSE);
        user.setInvestimento(InvestimentoENUM.MID);
        user.setMetas(new ArrayList<>());
        user.setExpenses(new ArrayList<>());
        user.setReceita(BigDecimal.ZERO);
        return user;
    }

    public void atualizarReceita(BigDecimal valor){
        if(valor.compareTo(BigDecimal.ZERO) < 0)throw new IllegalArgumentException("Receita não pode ser menor que 0");
        this.receita = valor;
    }

    public void atualizarInvestimento(InvestimentoENUM investimento){
        this.investimento = investimento;
    }

    public void verificarUsuario(boolean b){
        this.verificado = b;
    }

    public void garantirVerificado(){
        if(!this.verificado) throw new IllegalStateException("Usuário não verificado");
        this.verificado = true;
    }

    public void atualizarSenha(String senha){
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    private void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    private void setSenha(String senha) {
        this.senha = senha;
    }

    public BigDecimal getReceita() {
        return receita;
    }

    private void setReceita(BigDecimal receita) {
        this.receita = receita;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    private void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    private void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public List<ExpenseEntity> getExpenses() {
        return expenses;
    }

    private void setExpenses(List<ExpenseEntity> expenses) {
        this.expenses = expenses;
    }

    public List<MetasEntity> getMetas() {
        return metas;
    }

    private void setMetas(List<MetasEntity> metas) {
        this.metas = metas;
    }

    public InvestimentoENUM getInvestimento() {
        return investimento;
    }

    private void setInvestimento(InvestimentoENUM investimento) {
        this.investimento = investimento;
    }

    @JsonIgnore
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
