package com.devchaves.Pork_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devchaves.Pork_backend.DTO.ReceitaResponseDTO;
import com.devchaves.Pork_backend.DTO.UserUpdateDTO;
import com.devchaves.Pork_backend.services.ExpensesService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("expenses")
public class ExpenseController {

    private final ExpensesService expensesService;

    public ExpenseController(ExpensesService expensesService) {
        this.expensesService = expensesService;
    }

    @PostMapping("receita")
    public ResponseEntity<ReceitaResponseDTO> registrarReceita(@Valid @RequestBody UserUpdateDTO dto) {
        
        ReceitaResponseDTO receita = expensesService.adicionarReceita(dto);
        
        return ResponseEntity.ok(receita);
    }
    

}
