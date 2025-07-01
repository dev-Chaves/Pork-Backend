package com.devchaves.Pork_backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_despesas")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user")
    private UserEntity user;

    @Column(name = "receita", nullable = false)
    private Double receita;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private CategoriesENUM categoria;

}
