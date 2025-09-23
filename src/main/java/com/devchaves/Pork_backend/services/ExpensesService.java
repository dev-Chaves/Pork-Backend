package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.*;
import com.devchaves.Pork_backend.ENUM.CategoriasDeGastos;
import com.devchaves.Pork_backend.entity.ExpenseEntity;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.ExpenseRepository;
import com.devchaves.Pork_backend.repository.UserRepository;
import com.devchaves.Pork_backend.services.kafka.KafkaProducerService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpensesService {

    private static final Logger logger = LoggerFactory.getLogger(ExpensesService.class);

    private final ExpenseRepository expenseRepository;

    private final UserRepository userRepository;

    private final KafkaProducerService kafkaProducerService;

    public ExpensesService(ExpenseRepository expenseRepository, UserRepository userRepository, KafkaProducerService kafkaProducerService) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    public DashboardDTO consultarDespesasInfo(UserDetails userDetails){
        logger.info("Consultando informações do dashboard para o usuário: {}", userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        Long userId = user.getId();

        List<ExpenseEntity> todasDespesas = expenseRepository.findByUser(userId);
        List<ExpenseEntity> despesasFixas = expenseRepository.findFixedsExpensesByUserId(userId);
        List<ExpenseEntity> despesasVariaveis = expenseRepository.findVariablesExpensesByUserId(userId);
        BigDecimal despesasTotal = expenseRepository.sumTotalExpenseByUserId(userId);

        if(despesasTotal == null){
            despesasTotal = BigDecimal.ZERO;
        }

        List<ExpenseResponseDTO> despesaTotal = todasDespesas.stream().map(n -> new ExpenseResponseDTO(n.getId(), n.getValor(), n.getDescricao(), n.getCategoriasDeGastos())).toList();
        List<ExpenseResponseDTO> despesaCategoriaFixo = despesasFixas.stream().map(n -> new ExpenseResponseDTO(n.getId(), n.getValor(), n.getDescricao(), n.getCategoriasDeGastos())).toList();
        List<ExpenseResponseDTO> despesaCategoriaVariavel =
                despesasVariaveis.stream().map(n -> new ExpenseResponseDTO(n.getId(), n.getValor(), n.getDescricao(), n.getCategoriasDeGastos())).toList();

        logger.info("Consulta de informações do dashboard concluída para o usuário: {}", userDetails.getUsername());
        return new DashboardDTO(despesaTotal, despesaCategoriaVariavel, despesaCategoriaFixo, despesasTotal);
    }

    @Cacheable(value = "despesa_cache", key = "#userDetails.username" )
    public ExpenseListDTO consultarDespesas(UserDetails userDetails){
        logger.info("Consultando despesas para o usuário: {}. Verificando cache.", userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        List<ExpenseEntity> despesas = expenseRepository.findByUser(user.getId());
        logger.info("Consulta de despesas concluída para o usuário: {}. Encontradas {} despesas.", userDetails.getUsername(), despesas.size());
        return new ExpenseListDTO( despesas.stream().map((n)-> new ExpenseResponseDTO(n.getId(), n.getValor(),n.getDescricao(), n.getCategoriasDeGastos())).toList());
    }

    @Transactional
    @Caching( evict = {
            @CacheEvict(value = "despesa_cache", key = "#userDetails.username"),
    })
    public List<ExpenseResponseDTO> cadastrarDespesas(List<ExpenseRequestDTO> dtos, UserDetails userDetails){
        logger.info("Iniciando cadastro de {} despesas para o usuário: {}", dtos.size(), userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;

        if (!user.getVerificado()) {
            logger.warn("Tentativa de cadastrar despesa por usuário não verificado: {}", userDetails.getUsername());
            throw new IllegalStateException("Usuário não verificado!");
        }

        for (ExpenseRequestDTO dto : dtos) {
            if (dto == null) {
                logger.error("Tentativa de cadastrar despesa com valor nulo.");
                throw new IllegalArgumentException("Não deve conter valores nulos!");
            }
            if(dto.valor().compareTo(BigDecimal.ZERO) <= 0){
                logger.error("Tentativa de cadastrar despesa com valor não positivo: {}", dto.valor());
                throw new IllegalArgumentException("O valor da despesa deve ser maior que zero!");
            }
        }

        List<ExpenseEntity> despesas = new ArrayList<>();
        for(ExpenseRequestDTO dto : dtos){
            ExpenseEntity despesa = new ExpenseEntity();
            despesa.setUser(user);
            despesa.setValor(dto.valor());
            despesa.setDescricao(dto.descricao());
            despesa.setCategoriasDeGastos(dto.categoria());
            despesas.add(despesa);
        }

        expenseRepository.saveAll(despesas);
        logger.info("{} despesas cadastradas com sucesso para o usuário: {}", despesas.size(), userDetails.getUsername());

        return despesas.stream().map((despesa -> new ExpenseResponseDTO(
            despesa.getId(),
            despesa.getValor(),
            despesa.getDescricao(),
            despesa.getCategoriasDeGastos())))
        .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "despesa_cache", key = "#userDetails.username")
    public ExpenseResponseDTO atualizarDespesa(Long id, ExpenseRequestDTO dto, UserDetails userDetails ){
        logger.info("Iniciando atualização da despesa de ID {} para o usuário: {}", id, userDetails.getUsername());
        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> {
            logger.error("Usuário não encontrado ao tentar atualizar despesa: {}", userDetails.getUsername());
            return new UsernameNotFoundException("Usuário não encontrado");
        });
        Long userId = user.getId();

        ExpenseEntity despesa = expenseRepository.findByIdAndUserId(id, userId);
        
        if(despesa == null){
            logger.warn("Despesa de ID {} não encontrada para o usuário: {}", id, userDetails.getUsername());
            throw new IllegalArgumentException("Despesa não encontrada para o usuário fornecido.");
        }

        expenseRepository.updateDespesa(
                dto.valor(),
                dto.descricao(),
                dto.categoria().toString(),
                despesa.getId(),
                userId);
        logger.info("Despesa de ID {} atualizada com sucesso para o usuário: {}", id, userDetails.getUsername());
        return new ExpenseResponseDTO( despesa.getId(),dto.valor(), dto.descricao(), dto.categoria());
    }

    @CacheEvict(value = "despesa_cache", key = "#userDetails.username")
    public void apagarDespesa(Long id, UserDetails userDetails){
        logger.info("Iniciando exclusão da despesa de ID {} para o usuário: {}", id, userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        Long userId = user.getId();

        ExpenseEntity despesa = expenseRepository.findByIdAndUserId(id, userId);
        if(despesa == null){
            logger.warn("Tentativa de excluir despesa não existente (ID: {}) para o usuário: {}", id, userDetails.getUsername());
            return; 
        }

        expenseRepository.delete(despesa);
        logger.info("Despesa de ID {} excluída com sucesso para o usuário: {}", id, userDetails.getUsername());
    }

    @Cacheable(value = "receitaCache", key = "#userDetails.username")
    public ReceitaResponseDTO consultarReceita(UserDetails userDetails){
        logger.info("Consultando receita para o usuário: {}. Verificando cache.", userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        return new ReceitaResponseDTO(user.getReceita());
    }

    @Transactional
    @Caching( evict = {
            @CacheEvict(value = "receitaCache", key = "#userDetails.username"),
            @CacheEvict(value = "userDetailsCache", key = "#userDetails.username")
    })
    public ReceitaResponseDTO atualizarReceita(UserUpdateDTO dto, UserDetails userDetails){
        logger.info("Iniciando atualização de receita para o usuário: {}", userDetails.getUsername());
        UserEntity userD = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()-> {
            logger.error("Usuário não encontrado ao tentar atualizar receita: {}", userDetails.getUsername());
            return new UsernameNotFoundException("Usuário não encontrado");
        });
        Long user = userD.getId();

        if(dto.receita().compareTo(BigDecimal.ZERO) < 0){
            logger.error("Tentativa de atualizar receita com valor negativo: {}", dto.receita());
            throw new IllegalArgumentException("A receita não pode ser negativa!");
        }

        userRepository.updateReceita(user, dto.receita());
        logger.info("Receita atualizada com sucesso para o usuário: {}", userDetails.getUsername());

        String emailBody = String.format(
                "Olá %s,\n\n" +
                        "Notamos que você atualizou sua receita. Ótimas notícias!\n\n" +
                        "Uma mudança na receita é o momento perfeito para reavaliar suas metas e garantir que seu dinheiro está trabalhando para você da melhor forma possível. Com base no seu perfil %s, um novo plano de investimento pode otimizar seus ganhos.\n\n" +
                        "Que tal dar uma olhada no que preparamos para você? Clique abaixo para descobrir seu novo potencial de investimento.\n\n" +
                        "Estamos aqui para te ajudar a prosperar.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe Pork." ,
                userD.getNome(),
                userD.getInvestimento().toString()
        );

        EmailDTO emailDTO = new EmailDTO(userD.getEmail(), "Atualização de Receita - Reavalie seus investimentos!", emailBody);

        kafkaProducerService.sendEmailEvent(emailDTO);

        return new ReceitaResponseDTO(dto.receita());
    }

    public ExpenseListDTO consultarDespesasPorMesEntradaDeMes(int mes, UserDetails user){
        logger.info("Consultando despesas do mês {} para o usuário: {}", mes, user.getUsername());
        LocalDate diaUm = LocalDate.now().withMonth(mes).withDayOfMonth(1);
        LocalDate ultimoDia = diaUm.withDayOfMonth(diaUm.lengthOfMonth());
        LocalDateTime inicio = diaUm.atStartOfDay();
        LocalDateTime fim = ultimoDia.atTime(LocalTime.MAX);
        UserEntity userEntity = (UserEntity) user;

        List<ExpenseEntity> despesas = expenseRepository.findByDateRangeAndUserId(inicio, fim, userEntity.getId());
        logger.info("Encontradas {} despesas para o mês {} para o usuário: {}", despesas.size(), mes, user.getUsername());
        return new ExpenseListDTO(
                despesas.stream().map(
                        (d) -> new ExpenseResponseDTO(
                                d.getId(),
                                d.getValor(),
                                d.getDescricao(),
                                d.getCategoriasDeGastos()
                        )
                ).toList()
        );
    }

    public Page<ExpenseResponseDTO> consultarDespesasPaginadas(Pageable pageable, UserDetails userDetails){
        logger.info("Consultando despesas paginadas (página: {}, tamanho: {}) para o usuário: {}", pageable.getPageNumber(), pageable.getPageSize(), userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        Page<ExpenseEntity> despesas = expenseRepository.findByUserId(user.getId(), pageable);
        logger.info("Consulta paginada retornou {} elementos na página.", despesas.getNumberOfElements());
        return despesas.map(n -> new ExpenseResponseDTO(
                n.getId(),
                n.getValor(),
                n.getDescricao(),
                n.getCategoriasDeGastos()
        ));
    }

    public BigDecimal consultarValorDeGastosPorCategoria(CategoriasDeGastos categoriasDeGastos, int mes, UserDetails userDetails){
        logger.info("Consultando gastos da categoria {} no mês {} para o usuário: {}", categoriasDeGastos, mes, userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        List<ExpenseEntity> despesas = expenseRepository.findByUserIdAndCategoriasDeGastos(user.getId(), categoriasDeGastos);
        BigDecimal total = despesas.stream()
                .filter(c -> c.getCriadoEm().getMonth() == Month.of(mes) )
                .map(ExpenseEntity::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.info("Total de gastos para a categoria {} no mês {}: {}", categoriasDeGastos, mes, total);
        return total;
    }

    public BigDecimal consultarTotalGastoNoMes(int mes, UserDetails userDetails){
        logger.info("Consultando total de gastos no mês {} para o usuário: {}", mes, userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        PeriodoDTO periodo = periodoMensal(mes);
        List<ExpenseEntity> despesas = expenseRepository.findByDateRangeAndUserId(periodo.inicio(), periodo.fim(), user.getId());
        BigDecimal total = despesas.stream()
                .map(ExpenseEntity::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.info("Total de gastos no mês {}: {}", mes, total);
        return total;
    }

    public List<ExpenseResponseDTO> maiorGastoEmUmMes(int mes, UserDetails userDetails){
        logger.info("Consultando maior gasto no mês {} para o usuário: {}", mes, userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        PeriodoDTO periodo = periodoMensal(mes);
        List<ExpenseEntity> despesas = expenseRepository.findDespesasComMaiorValorNoPeriodo(periodo.inicio(), periodo.fim(), user.getId());
        logger.info("Encontrado(s) {} maior(es) gasto(s) no mês {}.", despesas.size(), mes);
        return despesas.stream()
                .map(d ->
                        new ExpenseResponseDTO(
                                d.getId(),
                                d.getValor(),
                                d.getDescricao(),
                                d.getCategoriasDeGastos())
                        ).toList();
    }

    private PeriodoDTO periodoMensal (int mes){
        LocalDate agora = LocalDate.now();
        LocalDate base = LocalDate.of(agora.getYear(), mes, 1);
        LocalDateTime inicio = base.atStartOfDay();
        LocalDateTime fim = base.plusMonths(1).atStartOfDay();
        return new PeriodoDTO(inicio, fim);
    }
}   
