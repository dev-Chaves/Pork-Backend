package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.*;
import com.devchaves.Pork_backend.entity.ExpenseEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.ExpenseRepository;
import com.devchaves.Pork_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpensesService {

    private static final Logger logger = LoggerFactory.getLogger(ExpensesService.class);

    private final ExpenseRepository expenseRepository;

    private final UserRepository userRepository;

    private final UtilServices utilServices;

    public ExpensesService(ExpenseRepository expenseRepository, UserRepository userRepository, UtilServices utilServices) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.utilServices = utilServices;
    }

    @Cacheable(value = "dashboard_cache", key = "#userDetails.username")
    public DashboardDTO consultarDespesasInfo(UserDetails userDetails){

        logger.info("Executando o método consultarDespesas(). Isso só deve aparecer no primeiro acesso ou após o cache ser invalidado.");

        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado"));

        Long userId = user.getId();

        List<ExpenseEntity> todasDespesas = expenseRepository.findByUser(userId);

        List<ExpenseEntity> despesasFixas = expenseRepository.findFixedsExpensesByUserId(userId);

        List<ExpenseEntity> despesasVariaveis = expenseRepository.findVariablesExpensesByUserId(userId);

        BigDecimal despesasTotal = expenseRepository.sumTotalExpenseByUserId(userId);

        if(despesasTotal == null){
            despesasTotal = BigDecimal.ZERO;
        }

        List<ExpenseResponseDTO> despesaTotal = todasDespesas.stream().map(n -> new ExpenseResponseDTO(n.getId(), n.getValor(), n.getDescricao(), n.getCategoria())).toList();

        List<ExpenseResponseDTO> despesaCategoriaFixo = despesasFixas.stream().map(n -> new ExpenseResponseDTO(n.getId(), n.getValor(), n.getDescricao(), n.getCategoria())).toList();

        List<ExpenseResponseDTO> despesaCategoriaVariavel =
                despesasVariaveis.stream().map(n -> new ExpenseResponseDTO(n.getId(), n.getValor(), n.getDescricao(), n.getCategoria())).toList();

        return new DashboardDTO(despesaTotal, despesaCategoriaVariavel, despesaCategoriaFixo, despesasTotal);
    }

    @Cacheable(value = "despesa_cache", key = "#userDetails.username" )
    public List<ExpenseResponseDTO> consultarDespesas(UserDetails userDetails){

        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado"));

        List<ExpenseEntity> despesas = expenseRepository.findByUser(user.getId());

        return despesas.stream().map((n)-> new ExpenseResponseDTO(n.getId(), n.getValor(),n.getDescricao(), n.getCategoria())).toList();

    }


    @CachePut(value = "despesa_cache", key = "#userDetails.username")
    public List<ExpenseResponseDTO> cadastrarDespesas(List<ExpenseRequestDTO> dtos, UserDetails userDetails){

        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado"));

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

    @Transactional
    @CacheEvict(value = "despesa_cache", key = "#userDetails.username")
    public ExpenseResponseDTO atualizarDespesa(Long id, ExpenseRequestDTO dto, UserDetails userDetails ){

        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado"));

        Long userId = user.getId();

        ExpenseEntity despesa = expenseRepository.findByIdAndUserId(id, userId);
        
        if(despesa == null){
            throw new IllegalArgumentException("Despesa não encontrada para o usuário fornecido.");
        }

        expenseRepository.updateDespesa(
                dto.valor(),
                dto.descricao(),
                dto.categoria().toString(),
                despesa.getId(),
                userId);

        return new ExpenseResponseDTO( despesa.getId(),dto.valor(), dto.descricao(), dto.categoria());

    }

    @CacheEvict(value = "despesa_cache", key = "#userDetails.username")
    public void apagarDespesa(Long id, UserDetails userDetails){

        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado"));

        Long userId = user.getId();

        ExpenseEntity despesa = expenseRepository.findByIdAndUserId(id, userId);

        expenseRepository.delete(despesa);

    }

    @Cacheable(value = "receitaCache", key = "#userDetails.username")
    public ReceitaResponseDTO consultarReceita(UserDetails userDetails){

        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado"));

        return new ReceitaResponseDTO(user.getReceita());

    }

    @Transactional
    @CacheEvict(value = "receitaCache", key = "#userDetails.username")
    public ReceitaResponseDTO atualizarReceita(UserUpdateDTO dto, UserDetails userDetails){

        UserEntity userD = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Usuário não encotrado"));

        Long user = userD.getId();

        userRepository.updateReceita(user, dto.receita());

        return new ReceitaResponseDTO(dto.receita());
    }

}   
