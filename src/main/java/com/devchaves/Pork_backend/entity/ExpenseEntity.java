package com.devchaves.Pork_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_despesas")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false )
    private UserEntity user;

    @Column(name = "receita", nullable = false)
    private Double receita;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private CategoriesENUM categoria;

    public ExpenseEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Double getReceita() {
        return receita;
    }

    public void setReceita(Double receita) {
        this.receita = receita;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CategoriesENUM getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriesENUM categoria) {
        this.categoria = categoria;
    }


}
