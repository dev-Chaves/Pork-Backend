package com.devchaves.Pork_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devchaves.Pork_backend.entity.ExpenseEntity;
import com.devchaves.Pork_backend.entity.UserEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    @Query(value = "SELECT * FROM tb_despesas WHERE categoria = :categoria", nativeQuery = true)
    public List<ExpenseEntity> findByCategory(String category);

    @Query(value = "SELECT * FROM tb_despesas WHERE usuario = :user", nativeQuery = true)
    public List<ExpenseEntity> findByUser(UserEntity user);

}
