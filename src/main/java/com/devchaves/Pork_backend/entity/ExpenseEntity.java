package com.devchaves.Pork_backend.entity;

import com.devchaves.Pork_backend.DTO.ExpenseRequestDTO;
import com.devchaves.Pork_backend.ENUM.CategoriasDeGastos;
import com.devchaves.Pork_backend.ENUM.CategoriesENUM;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_despesas")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false )
    private UserEntity user;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Enumerated(EnumType.STRING)
    private CategoriesENUM categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "categorias")
    private CategoriasDeGastos categoriasDeGastos;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    protected ExpenseEntity() {
    }

    public static ExpenseEntity from(ExpenseRequestDTO dto, UserEntity user){

        if(dto.valor().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Valor deve ser maior que 0");

        ExpenseEntity expense = new ExpenseEntity();

        expense.setUser(user);

        expense.setValor(dto.valor());

        expense.setCriadoEm(LocalDateTime.now());

        expense.setAtualizadoEm(LocalDateTime.now());

        expense.setCategoriasDeGastos(dto.categoria());

        return expense;

    }

    public void adicionarValor(BigDecimal valor){
        if(valor.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Valor deve ser maior que 0");
        this.valor = valor;
    }

    public void atualizarDespesa(){
        this.atualizadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    private void setUser(UserEntity user) {
        this.user = user;
    }

    public BigDecimal getValor() {
        return valor;
    }

    private void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    private void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    private void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    private void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public CategoriesENUM getCategoria() {
        return categoria;
    }

    private void setCategoria(CategoriesENUM categoria) {
        this.categoria = categoria;
    }

    public CategoriasDeGastos getCategoriasDeGastos() {
        return categoriasDeGastos;
    }

    private void setCategoriasDeGastos(CategoriasDeGastos categoriasDeGastos) {
        this.categoriasDeGastos = categoriasDeGastos;
    }
}
