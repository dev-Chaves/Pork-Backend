package com.devchaves.Pork_backend.controller;

import java.util.List;

import com.devchaves.Pork_backend.DTO.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("consultar-receita")
    public ResponseEntity<ReceitaResponseDTO> consultarReceita(@AuthenticationPrincipal UserDetails userDetails){
        ReceitaResponseDTO response = expensesService.consultarReceita(userDetails);
        return ResponseEntity.ok(response);
    }

    @PutMapping("atualizar-receita")
    public ResponseEntity<ReceitaResponseDTO> atualizarReceita(@Valid @RequestBody UserUpdateDTO dto, @AuthenticationPrincipal UserDetails userDetails){

        ReceitaResponseDTO respose = expensesService.atualizarReceita(dto, userDetails);

        return ResponseEntity.ok(respose);
    }

    @PostMapping("anotar-despesas")
    public ResponseEntity<List<ExpenseResponseDTO>> registrarDespesas(@Valid @RequestBody List<ExpenseRequestDTO> dto, @AuthenticationPrincipal UserDetails userDetails) {
    
        List<ExpenseResponseDTO> responses = expensesService.cadastrarDespesas(dto, userDetails);
        
        return ResponseEntity.ok(responses);
    }

    @GetMapping("consultar-despesas")
    public ResponseEntity<ExpenseListDTO> consultarDespesas(@AuthenticationPrincipal UserDetails userDetails) {
        ExpenseListDTO response = expensesService.consultarDespesas(userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("consultar-despesas-completa")
    public ResponseEntity<DashboardDTO> despesaCompleta(@AuthenticationPrincipal UserDetails userDetails){
        DashboardDTO response = expensesService.consultarDespesasInfo(userDetails);
        return ResponseEntity.ok( response );
    }

    @GetMapping("consultar-gastos")
    public ResponseEntity<String> consultarGastos(@AuthenticationPrincipal UserDetails userDetails){

        String result = expensesService.consultarDespensasJson(userDetails);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @DeleteMapping("apagar-despesa/{id}")
    public ResponseEntity<String> apagarDespesa(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){

        expensesService.apagarDespesa(id, userDetails);

        return ResponseEntity.ok().body("Apagado com sucesso!");

    }

    @PutMapping("atualizar-despesa/{id}")
    public ResponseEntity<ExpenseResponseDTO> atualizarDespesa(@PathVariable Long id, @Valid @RequestBody ExpenseRequestDTO dto, @AuthenticationPrincipal UserDetails userDetails){
        ExpenseResponseDTO response = expensesService.atualizarDespesa(id, dto, userDetails);
        return ResponseEntity.ok(response);
    } 

}
