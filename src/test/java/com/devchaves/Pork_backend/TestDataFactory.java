package com.devchaves.Pork_backend;

import com.devchaves.Pork_backend.ENUM.CategoriasDeGastos;
import com.devchaves.Pork_backend.ENUM.InvestimentoENUM;
import com.devchaves.Pork_backend.entity.ExpenseEntity;
import com.devchaves.Pork_backend.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {}

    public static UserEntity mockUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setNome("João Silva");
        user.setEmail("joao.silva@example.com");
        user.setSenha("hashed-password");
        user.setReceita(new BigDecimal("8500.00"));
        user.setVerificado(true);
        user.setCriadoEm(LocalDateTime.of(2024, 1, 10, 9, 30));
        user.setInvestimento(InvestimentoENUM.MID); // ajuste se quiser outro valor

        return user;
    }

    public static ExpenseEntity mockExpense(
            Long id,
            UserEntity user,
            String descricao,
            String valor,
            CategoriasDeGastos categoriasDeGastos,
            LocalDateTime criadoEm
    ) {
        ExpenseEntity e = new ExpenseEntity();
        e.setId(id);
        e.setUser(user);
        e.setDescricao(descricao);
        e.setValor(new BigDecimal(valor));
        e.setCategoriasDeGastos(categoriasDeGastos);
        e.setCriadoEm(criadoEm);
        return e;
    }

    public static List<ExpenseEntity> mockSixExpenses(UserEntity user) {
        return Arrays.asList(
                mockExpense(
                        101L,
                        user,
                        "Supermercado do mês",
                        "450.75",
                         CategoriasDeGastos.ALIMENTACAO,
                        LocalDateTime.of(2024, 2, 1, 12, 0)
                ),
                mockExpense(
                        102L, user, "Transporte (app)", "37.90", CategoriasDeGastos.TRANSPORTE,
                        LocalDateTime.of(2024, 2, 2, 8, 30)
                ),
                mockExpense(
                        103L, user, "Aluguel", "1800.00",CategoriasDeGastos.ALIMENTACAO,
                        LocalDateTime.of(2024, 2, 5, 10, 0)
                ),
                mockExpense(
                        104L, user, "Plano de saúde", "420.00",
                        CategoriasDeGastos.OUTROS,
                        LocalDateTime.of(2024, 2, 7, 9, 15)
                ),
                mockExpense(
                        105L, user, "Cinema", "52.00",
                        CategoriasDeGastos.LAZER,
                        LocalDateTime.of(2024, 2, 10, 20, 45)
                ),
                mockExpense(
                        106L, user, "Curso online", "199.90",
                        CategoriasDeGastos.CONTAS,
                        LocalDateTime.of(2024, 2, 12, 19, 0)
                )
        );
    }
}