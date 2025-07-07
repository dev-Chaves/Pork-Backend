package com.devchaves.Pork_backend.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devchaves.Pork_backend.DTO.ExpenseRequestDTO;
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

    public List<ExpenseEntity> cadastrarDespesas(ExpenseRequestDTO dto){

       UserEntity user = getCurrentUser();

       if (user.getVerificado() == false) {
        throw new IllegalStateException("Usuário não verificado!");
       }

       ExpenseEntity despesas = new ExpenseEntity(); 

    //    despesas.setUser(user);
    //    despesas.setReceita(dto.receita());
    //    despesas.

        return null;

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
