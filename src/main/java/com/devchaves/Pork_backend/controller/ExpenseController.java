package com.devchaves.Pork_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devchaves.Pork_backend.DTO.DashboardDTO;
import com.devchaves.Pork_backend.DTO.ExpenseRequestDTO;
import com.devchaves.Pork_backend.DTO.ExpenseResponseDTO;
import com.devchaves.Pork_backend.DTO.ReceitaResponseDTO;
import com.devchaves.Pork_backend.DTO.UserUpdateDTO;
import com.devchaves.Pork_backend.services.ExpensesService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/despesas")
@Tag(name = "Despesas e Receitas", description = "Endpoints para gerenciamento de receitas e despesas do usuário")
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

    @GetMapping("consultar-receita")
    public ResponseEntity<ReceitaResponseDTO> consultarReceita(){
        ReceitaResponseDTO response = expensesService.consultarReceita();
        return ResponseEntity.ok(response);
    }

    @PutMapping("atualizar-receita")
    public ResponseEntity<ReceitaResponseDTO> atualizarReceita(@Valid @RequestBody UserUpdateDTO dto){

        ReceitaResponseDTO respose = expensesService.atualizarReceita(dto);

        return ResponseEntity.ok(respose);
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
