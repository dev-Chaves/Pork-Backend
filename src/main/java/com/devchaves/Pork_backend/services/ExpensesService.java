package com.devchaves.Pork_backend.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devchaves.Pork_backend.DTO.DashboardDTO;
import com.devchaves.Pork_backend.DTO.ExpenseRequestDTO;
import com.devchaves.Pork_backend.DTO.ExpenseResponseDTO;
import com.devchaves.Pork_backend.DTO.ReceitaResponseDTO;
import com.devchaves.Pork_backend.DTO.UserUpdateDTO;
import com.devchaves.Pork_backend.ENUM.CategoriesENUM;
import com.devchaves.Pork_backend.entity.ExpenseEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.ExpenseRepository;
import com.devchaves.Pork_backend.repository.UserRepository;

@Service
public class ExpensesService {

    private final ExpenseRepository expenseRepository;

    private final UserRepository userRepository;

    private final UtilServices utilServices;

    public ExpensesService(ExpenseRepository expenseRepository, UserRepository userRepository, UtilServices utilServices) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.utilServices = utilServices;
    }

    public List<ExpenseResponseDTO> cadastrarDespesas(List<ExpenseRequestDTO> dtos){

       UserEntity user = utilServices.getCurrentUser();

       System.out.println(user.getEmail());

       if (!user.getVerificado()) {
        throw new IllegalStateException("Usuário não verificado!");
       }

       for (ExpenseRequestDTO dto : dtos) {
           if (dto == null) {
                throw new IllegalArgumentException("Não deve conter valores nulos!");
           }

            if(dto.valor().compareTo(BigDecimal.ZERO) <= 0){
                throw new IllegalArgumentException("O valor da despesa deve ser maior que zero!");
            }
       }

       List<ExpenseEntity> despesas = new ArrayList<>();

       for(ExpenseRequestDTO dto : dtos){
        ExpenseEntity despesa = new ExpenseEntity();
        despesa.setUser(user);
        despesa.setValor(dto.valor());
        despesa.setDescricao(dto.descricao());
        despesa.setCategoria(dto.categoria());
        despesas.add(despesa);
       }

        expenseRepository.saveAll(despesas);

        return despesas.stream().map((despesa -> new ExpenseResponseDTO(
        despesa.getId(),
        despesa.getValor(),
        despesa.getDescricao(),
        despesa.getCategoria())))
        .collect(Collectors.toList());

    }

    public ReceitaResponseDTO adicionarReceita(UserUpdateDTO dto){

        UserEntity user = utilServices.getCurrentUser();

        if (!user.getVerificado()) {
            throw new IllegalStateException("Usuário não verificado!");
        }

        System.out.println(user.getUsername());

        System.out.println(dto.receita());

        user.setReceita(dto.receita());

        userRepository.save(user);

        return new ReceitaResponseDTO(user.getReceita());

    }

    public DashboardDTO consultarDespesas(){
    
        UserEntity user = utilServices.getCurrentUser();

        List<ExpenseEntity> despesas = expenseRepository.findByUser(user.getId());

        List<ExpenseResponseDTO> despesaTotal = despesas.stream().map(n -> new ExpenseResponseDTO(n.getId(), n.getValor(), n.getDescricao(), n.getCategoria())).toList();

        List<ExpenseResponseDTO> despesaCategoriaFixo = despesas.stream()
                .filter(n -> n.getCategoria().equals(CategoriesENUM.FIXA))
                .map(s -> new ExpenseResponseDTO(s.getId(),s.getValor(),s.getDescricao(),s.getCategoria())).toList();

        List<ExpenseResponseDTO> despesaCategoriaVariavel = despesas.stream()
                .filter(n -> n.getCategoria().equals(CategoriesENUM.VARIAVEL))
                .map(s -> new ExpenseResponseDTO(s.getId(),s.getValor(),s.getDescricao(),s.getCategoria())).toList();

        BigDecimal totalDespesas = despesas.stream().map(ExpenseEntity::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardDTO(despesaTotal, despesaCategoriaVariavel, despesaCategoriaFixo, totalDespesas);
    }

    public ExpenseResponseDTO atualizarDespesa(Long id, ExpenseRequestDTO dto ){

        UserEntity user = utilServices.getCurrentUser();

        ExpenseEntity despesa = expenseRepository.findByIdAndUserId(id, user.getId());
        
        if(despesa == null){
            throw new IllegalArgumentException("Despesa não encontrada para o usuário fornecido.");
        }

        despesa.setValor(dto.valor());
        despesa.setDescricao(dto.descricao());
        despesa.setCategoria(dto.categoria());
        despesa.setAtualizadoEm(LocalDateTime.now());

        expenseRepository.save(despesa);

        return new ExpenseResponseDTO( despesa.getId(),despesa.getValor(), despesa.getDescricao(), despesa.getCategoria());

    }

    public void apagarDespesa(Long id){

        UserEntity user = utilServices.getCurrentUser();

        ExpenseEntity despesa = expenseRepository.findByIdAndUserId(id, user.getId());

        expenseRepository.delete(despesa);

    }

    public ReceitaResponseDTO consultarReceita(){

        UserEntity user = utilServices.getCurrentUser();

        return new ReceitaResponseDTO(user.getReceita());
    }

    public ReceitaResponseDTO atualizarReceita(UserUpdateDTO dto){

        UserEntity user = utilServices.getCurrentUser();

        user.setReceita(dto.receita());

        userRepository.save(user);

        return new ReceitaResponseDTO(user.getReceita());
    }

}   
