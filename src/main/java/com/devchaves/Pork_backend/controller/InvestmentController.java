package com.devchaves.Pork_backend.controller;

import com.devchaves.Pork_backend.DTO.InvestmentMethodsResponse;
import com.devchaves.Pork_backend.DTO.InvestmentRequestDTO;
import com.devchaves.Pork_backend.DTO.InvestmentResponseDTO;
import com.devchaves.Pork_backend.services.InvestmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("investimento")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @PutMapping("alterar-investimento")
    public ResponseEntity<InvestmentResponseDTO> alterarTipoDeInvestimento(@Valid @RequestBody InvestmentRequestDTO dto, @AuthenticationPrincipal UserDetails userDetails){
        InvestmentResponseDTO response = investmentService.selecionarInvestimento(dto, userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("consultar-investimento")
    public ResponseEntity<InvestmentMethodsResponse> consultarInvestimento(@AuthenticationPrincipal UserDetails userDetails){
        InvestmentMethodsResponse response = investmentService.calcularInvestimentos(userDetails);
        return ResponseEntity.ok(response);
    }

}
