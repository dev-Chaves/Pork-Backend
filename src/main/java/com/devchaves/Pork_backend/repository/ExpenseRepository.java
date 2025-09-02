package com.devchaves.Pork_backend.repository;

import com.devchaves.Pork_backend.ENUM.CategoriasDeGastos;
import com.devchaves.Pork_backend.ENUM.CategoriesENUM;
import com.devchaves.Pork_backend.entity.ExpenseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    @Query(value = "SELECT * FROM tb_despesas WHERE categoria = :categoria", nativeQuery = true)
    List<ExpenseEntity> findByCategory(String category);

    @Query(value = "SELECT * FROM tb_despesas WHERE user_id = :userId", nativeQuery = true)
    List<ExpenseEntity> findByUser(Long userId);

    @Query(value = "SELECT * FROM tb_despesas WHERE id = :id AND user_id = :userId", nativeQuery = true)
    ExpenseEntity findByIdAndUserId(Long id, Long userId);

    @Query(value = "SELECT * FROM tb_despesas WHERE user_id = :userId AND categoria = 'VARIAVEL'", nativeQuery = true)
    List<ExpenseEntity> findVariablesExpensesByUserId(Long userId);

    @Query(value = "SELECT * FROM tb_despesas WHERE user_id = :userId AND categoria = 'FIXA'", nativeQuery = true)
    List<ExpenseEntity> findFixedsExpensesByUserId(Long userId);

    @Query(value = "SELECT SUM(valor) FROM tb_despesas WHERE user_id = :userId", nativeQuery = true)
    BigDecimal sumTotalExpenseByUserId(Long userId);

    @Modifying
    @Query(value = "UPDATE tb_despesas SET valor = :valor, descricao = :descricao, categoria = :categoria, atualizado_em = CURRENT_TIMESTAMP WHERE id = :id AND user_id = :userId", nativeQuery = true)
    void updateDespesa(
            @Param("valor") BigDecimal valor,
            @Param("descricao")String descricao,
            @Param("categoria")String categoria,
            @Param("id")Long id,
            @Param("userId")Long userId);

    @Query(value = "SELECT d FROM ExpenseEntity d WHERE d.criadoEm BETWEEN :dataInicio AND :dataFim AND d.user.id = :userId")
     List<ExpenseEntity> findByDateRangeAndUserId(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim, @Param("userId") Long userId);

    @Query(value = """
            SELECT d
            FROM ExpenseEntity d
            WHERE d.user.id = :userId
            AND d.criadoEm BETWEEN :dataInicio AND :dataFim
            AND d.valor = (
                SELECT MAX(d2.valor)
                FROM ExpenseEntity d2
                WHERE d2.user.id = :userId
                AND d2.criadoEm BETWEEN :dataInicio AND :dataFim
            )
            """)
    List<ExpenseEntity> findDespesasComMaiorValorNoPeriodo(
            @Param("dataInicio") LocalDateTime inicio,
            @Param("dataFim") LocalDateTime fim,
            @Param("userId") Long userId
    );

    Page<ExpenseEntity> findByUserId(Long userId, Pageable pageable);

    List<ExpenseEntity> findByUserIdAndCategoriasDeGastos(Long userId, CategoriasDeGastos categoriasDeGastos);
}
