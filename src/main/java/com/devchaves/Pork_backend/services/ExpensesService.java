package com.devchaves.Pork_backend.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devchaves.Pork_backend.DTO.ExpenseRequestDTO;
import com.devchaves.Pork_backend.DTO.ExpenseResponseDTO;
import com.devchaves.Pork_backend.DTO.ReceitaResponseDTO;
import com.devchaves.Pork_backend.DTO.UserUpdateDTO;
import com.devchaves.Pork_backend.entity.ExpenseEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.ExpenseRepository;
import com.devchaves.Pork_backend.repository.UserRepository;

@Service
public class ExpensesService {

    private final ExpenseRepository expenseRepository;

    private final UserRepository userRepository;

    public ExpensesService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public List<ExpenseResponseDTO> cadastrarDespesas(List<ExpenseRequestDTO> dtos){

       UserEntity user = getCurrentUser();

       if (user.getVerificado() == false) {
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

       List<ExpenseResponseDTO> response = despesas.stream().map((despesa -> new ExpenseResponseDTO(
        despesa.getId(), 
        despesa.getValor(),
        despesa.getDescricao(),
        despesa.getCategoria())))
        .collect(Collectors.toList());

       return response;

    }

    public ReceitaResponseDTO adicionarReceita(UserUpdateDTO dto){

        UserEntity user = getCurrentUser();

        System.out.println(user.getUsername());

        System.out.println(dto.receita());

        user.setReceita(dto.receita());

        userRepository.save(user);

        ReceitaResponseDTO response = new ReceitaResponseDTO(user.getReceita());

        return response;

    }


    private UserEntity getCurrentUser (){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication != null && authentication.getPrincipal() instanceof UserEntity){
                return (UserEntity) authentication.getPrincipal();
            }
        }catch(Exception e){
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
        
        return null;
    }

}   
