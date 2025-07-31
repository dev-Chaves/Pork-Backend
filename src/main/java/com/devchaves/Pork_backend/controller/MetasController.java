package com.devchaves.Pork_backend.controller;

import com.devchaves.Pork_backend.DTO.MetasRequestDTO;
import com.devchaves.Pork_backend.DTO.MetasResponseDTO;
import com.devchaves.Pork_backend.services.MetasService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<MetasResponseDTO>> cadastrarMetas(@Valid @RequestBody List<MetasRequestDTO> dtos){
        return ResponseEntity.ok(metasService.cadastrarMetas(dtos));
    }

    @PutMapping("atualizar-meta")
    public ResponseEntity<MetasResponseDTO> atualizarMeta(@PathVariable Long id, @Valid @RequestBody MetasRequestDTO dto){
        return ResponseEntity.ok(metasService.alterarMeta(id, dto));
    }

    @DeleteMapping("deletar-meta")
    public ResponseEntity<String> apagarMeta(@PathVariable Long id){
        metasService.apagarMeta(id);
        return ResponseEntity.ok("Apagado com sucesso!");
    }

    @GetMapping("consultar-metas")
    public ResponseEntity<List<MetasResponseDTO>> consultarMetas(){
        return ResponseEntity.ok(metasService.consultarMetas());
    }

}
