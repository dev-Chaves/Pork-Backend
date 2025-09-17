package com.devchaves.Pork_backend;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.devchaves.Pork_backend.DTO.*;
import com.devchaves.Pork_backend.ENUM.CategoriasDeGastos;
import com.devchaves.Pork_backend.entity.ExpenseEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.ExpenseRepository;
import com.devchaves.Pork_backend.repository.UserRepository;
import com.devchaves.Pork_backend.services.ExpensesService;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpensesService expensesService;

    @Test
    void deveRetornarMaiorGastoDoMes(){

        UserEntity user = TestDataFactory.mockUser();

        List<ExpenseEntity> top = List.of(
                buildExpense(user, 1L, "Aluguel", "1800.00", CategoriasDeGastos.CONTAS),
                buildExpense(user, 2L, "Curso", "199.90", CategoriasDeGastos.OUTROS)
        );

        when(expenseRepository.findDespesasComMaiorValorNoPeriodo(any(LocalDateTime.class), any(LocalDateTime.class), eq(user.getId()))).thenReturn(top);

        when(expenseRepository.findByDateRangeAndUserId(any(LocalDateTime.class), any(LocalDateTime.class), eq(user.getId())));

        List<ExpenseResponseDTO> response = expensesService.maiorGastoEmUmMes(2, user);

        assertThat(response).hasSize(2);
        assertThat(response.get(0).descricao()).isEqualTo("Aluguel");

        verify(expenseRepository).findDespesasComMaiorValorNoPeriodo(any(LocalDateTime.class), any(LocalDateTime.class), eq(user.getId()));

    }

    private ExpenseEntity buildExpense(UserEntity user, Long id, String desc, String valor, CategoriasDeGastos cat) {
        ExpenseEntity e = new ExpenseEntity();
        e.setId(id);
        e.setUser(user);
        e.setDescricao(desc);
        e.setValor(new BigDecimal(valor));
        e.setCategoriasDeGastos(cat);
        e.setCriadoEm(LocalDateTime.now());
        e.setAtualizadoEm(LocalDateTime.now());
        return e;
    }

}
