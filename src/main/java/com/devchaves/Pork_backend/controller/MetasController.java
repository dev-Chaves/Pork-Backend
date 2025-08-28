package com.devchaves.Pork_backend.controller;

import com.devchaves.Pork_backend.DTO.MetasRequestDTO;
import com.devchaves.Pork_backend.DTO.MetasResponseDTO;
import com.devchaves.Pork_backend.services.MetasService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metas")
public class MetasController {

    private final MetasService metasService;

    public MetasController(MetasService metasService) {
        this.metasService = metasService;
    }

    @PostMapping("cadastrar-metas")
    public ResponseEntity<List<MetasResponseDTO>> cadastrarMetas(@Valid @RequestBody List<MetasRequestDTO> dtos, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(metasService.cadastrarMetas(dtos, userDetails));
    }

    @PutMapping("atualizar-meta/{id}")
    public ResponseEntity<MetasResponseDTO> atualizarMeta(@PathVariable Long id, @Valid @RequestBody MetasRequestDTO dto, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(metasService.alterarMeta(id, dto, userDetails));
    }

    @DeleteMapping("deletar-meta/{id}")
    public ResponseEntity<String> apagarMeta(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        metasService.apagarMeta(id, userDetails);
        return ResponseEntity.ok("Apagado com sucesso!");
    }

    @GetMapping("consultar-metas")
    public ResponseEntity<List<MetasResponseDTO>> consultarMetas(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(metasService.consultarMetas(userDetails));
    }

    @GetMapping("consultar-metas-paginadas")
    public ResponseEntity<Page<MetasResponseDTO>> consultarMetasPaginadas(
            @RequestParam int pageNo,
            @RequestParam int pageSize,
            @AuthenticationPrincipal UserDetails userDetails
    ){

        return ResponseEntity.ok(metasService.consultarMetasPaginadas(pageNo, pageSize, userDetails));

    }

}
