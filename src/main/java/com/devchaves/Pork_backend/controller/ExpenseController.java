package com.devchaves.Pork_backend.controller;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devchaves.Pork_backend.DTO.DashboardDTO;
import com.devchaves.Pork_backend.DTO.ExpenseRequestDTO;
import com.devchaves.Pork_backend.DTO.ExpenseResponseDTO;
import com.devchaves.Pork_backend.DTO.ReceitaResponseDTO;
import com.devchaves.Pork_backend.DTO.UserUpdateDTO;
import com.devchaves.Pork_backend.services.ExpensesService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/despesas")
public class ExpenseController {

    private final ExpensesService expensesService;

    public ExpenseController(ExpensesService expensesService) {
        this.expensesService = expensesService;
    }

    @PostMapping("anotar-receita")
    public ResponseEntity<ReceitaResponseDTO> registrarReceita(@Valid @RequestBody UserUpdateDTO dto) {
        
        ReceitaResponseDTO receita = expensesService.adicionarReceita(dto);
        
        return ResponseEntity.ok(receita);
    }

    @PostMapping("anotar-despesas")
    public ResponseEntity<List<ExpenseResponseDTO>> registrarDespesas(@Valid @RequestBody List<ExpenseRequestDTO> dto) {
    
        List<ExpenseResponseDTO> responses = expensesService.cadastrarDespesas(dto);
        
        return ResponseEntity.ok(responses);
    }

    @GetMapping("consultar-despesas")
    public ResponseEntity<DashboardDTO> consultarDespesas() {
        DashboardDTO response = expensesService.consultarDespesas();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("apagar-despesa/{id}")
    public ResponseEntity<String> apagarDespesa(@PathVariable Long id){

        expensesService.apagarDespesa(id);

        return ResponseEntity.ok().body("Apagado com sucesso!");

    }

    @PutMapping("atualizar-despesa/{id}")
    public ResponseEntity<ExpenseResponseDTO> atualizarDespesa(@PathVariable Long id, @Valid @RequestBody ExpenseRequestDTO dto){
        ExpenseResponseDTO response = expensesService.atualizarDespesa(id, dto);
        return ResponseEntity.ok(response);
    } 

}
