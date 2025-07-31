package com.devchaves.Pork_backend.controller;

import com.devchaves.Pork_backend.DTO.InvestmentMethodsResponse;
import com.devchaves.Pork_backend.DTO.InvestmentRequestDTO;
import com.devchaves.Pork_backend.DTO.InvestmentResponseDTO;
import com.devchaves.Pork_backend.services.InvestmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("investimento")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

//    @PostMapping("adicionar-investimento")
//    public ResponseEntity<InvestmentResponseDTO> selecionarTipoDeInvestimento(@Valid @RequestBody InvestmentRequestDTO dto){
//        InvestmentResponseDTO response = investmentService.selecionarInvestimento(dto);
//        return ResponseEntity.ok(response);
//    }

    @PutMapping("alterar-investimento")
    public ResponseEntity<InvestmentResponseDTO> alterarTipoDeInvestimento(@Valid @RequestBody InvestmentRequestDTO dto){
        InvestmentResponseDTO response = investmentService.selecionarInvestimento(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("consultar-investimento")
    public ResponseEntity<InvestmentMethodsResponse> consultarInvestimento(){
        InvestmentMethodsResponse response = investmentService.calcularInvestimentos();
        return ResponseEntity.ok(response);
    }

}
