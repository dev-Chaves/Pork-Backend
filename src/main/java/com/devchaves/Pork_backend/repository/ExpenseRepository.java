package com.devchaves.Pork_backend.repository;

import com.devchaves.Pork_backend.ENUM.CategoriesENUM;
import com.devchaves.Pork_backend.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    @Query(value = "SELECT * FROM tb_despesas WHERE categoria = :categoria", nativeQuery = true)
    public List<ExpenseEntity> findByCategory(String category);

    @Query(value = "SELECT * FROM tb_despesas WHERE user_id = :userId", nativeQuery = true)
    public List<ExpenseEntity> findByUser(Long userId);

    @Query(value = "SELECT * FROM tb_despesas WHERE id = :id AND user_id = :userId", nativeQuery = true)
    public ExpenseEntity findByIdAndUserId(Long id, Long userId);

    @Query(value = "SELECT * FROM tb_despesas WHERE user_id = :user_id AND categoria = 'VARIAVEL'", nativeQuery = true)
    public List<ExpenseEntity> findVariablesExpensesByUserId(Long userId);

    @Query(value = "SELECT * FROM tb_despesas WHERE user_id = :user_id AND categoria = 'FIXA'", nativeQuery = true)
    public List<ExpenseEntity> findFixedsExpensesByUserId(Long userId);

    @Query(value = "SELECT SUM(valor) FROM tb_despesas WHERE user_id = :userId", nativeQuery = true)
    public BigDecimal sumTotalExpenseByUserId(Long userId);

    @Modifying
    @Query(value = "UPDATE tb_despesas SET valor = :valor, descricao =: descricao, categoria = :categoria, atualizado_em = CURRENT_TIMESTAMP WHERE id = :id AND user_id = :userId", nativeQuery = true)
    void updateDespesa(
            @Param("valor") BigDecimal valor,
            @Param("descricao")String descricao,
            @Param("categoria")String categoria,
            @Param("id")Long id,
            @Param("userId")Long userId);

}
