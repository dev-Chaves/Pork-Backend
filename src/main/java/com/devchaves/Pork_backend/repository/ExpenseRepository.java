package com.devchaves.Pork_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devchaves.Pork_backend.entity.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    @Query(value = "SELECT * FROM tb_despesas WHERE categoria = :categoria", nativeQuery = true)
    public List<ExpenseEntity> findByCategory(String category);

    @Query(value = "SELECT * FROM tb_despesas WHERE user_id = :userId", nativeQuery = true)
    public List<ExpenseEntity> findByUser(Long userId);

}
