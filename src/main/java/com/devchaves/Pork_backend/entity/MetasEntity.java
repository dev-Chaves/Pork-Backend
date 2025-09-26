package com.devchaves.Pork_backend.entity;

import com.devchaves.Pork_backend.DTO.MetasRequestDTO;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_metas")
public class MetasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false )
    private UserEntity user;

    @Column(nullable = false)
    private String meta;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate data;

    protected MetasEntity() {
    }

    public static MetasEntity from (MetasRequestDTO dto, UserEntity user){
        MetasEntity meta = new MetasEntity();
        meta.setUser(user);
        meta.setMeta(dto.meta());
        meta.setData(dto.data());
        meta.setValor(dto.valor());
        return meta;
    }

    public void alterarData (LocalDate date){
        this.data = date;
    }

    public void alterarNomeDaMeta(String nome){
        this.meta = nome;
    }

    public void alterarValorDaMeta(BigDecimal valor){

        if(valor.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Valor deve ser maior que 0");

        this.valor = valor;

    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getMeta() {
        return meta;
    }

    private void setMeta(String meta) {
        this.meta = meta;
    }

    public BigDecimal getValor() {
        return valor;
    }

    private void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public UserEntity getUser() {
        return user;
    }

    private void setUser(UserEntity user) {
        this.user = user;
    }

    public LocalDate getData() {
        return data;
    }

    private void setData(LocalDate data) {
        this.data = data;
    }
}
