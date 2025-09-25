package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.InvestmentMethodsResponse;
import com.devchaves.Pork_backend.DTO.InvestmentRequestDTO;
import com.devchaves.Pork_backend.DTO.InvestmentResponseDTO;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Stream;

@Service
public class InvestmentService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(InvestmentService.class);

    public InvestmentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final List<BigDecimal> porcentagem = Stream.of(10,30,50).map(BigDecimal::new).toList();

    @Transactional
    @CacheEvict(value = "userDetailsCache", key = "#userDetails.username")
    public InvestmentResponseDTO selecionarInvestimento(InvestmentRequestDTO dto, UserDetails userDetails){
        logger.info("Usuário {} selecionou o tipo de investimento: {}", userDetails.getUsername(), dto.tipo());
        UserEntity user = (UserEntity) userDetails;

        user.atualizarInvestimento(dto.tipo());

        userRepository.save(user);

        logger.info("Tipo de investimento atualizado com sucesso para o usuário: {}", userDetails.getUsername());

        return new InvestmentResponseDTO(dto.tipo().toString());
    }

    public InvestmentMethodsResponse calcularInvestimentos(UserDetails userDetails){
        logger.info("Calculando investimentos para o usuário: {}", userDetails.getUsername());
        UserEntity user = (UserEntity) userDetails;
        String investimentoENUM = user.getInvestimento().toString();
        BigDecimal receita = user.getReceita();
        BigDecimal valorInvestimento;

        switch (investimentoENUM) {
            case "HARD":
                valorInvestimento = receita.multiply(porcentagem.get(2).divide(new BigDecimal(100))).setScale(0, RoundingMode.CEILING);
                logger.info("Cálculo para perfil HARD: {}% de {} = {}", porcentagem.get(2), receita, valorInvestimento);
                return new InvestmentMethodsResponse(user.getInvestimento(), valorInvestimento);
            case "MID":
                valorInvestimento = receita.multiply(porcentagem.get(1).divide(new BigDecimal(100))).setScale(0, RoundingMode.CEILING);
                logger.info("Cálculo para perfil MID: {}% de {} = {}", porcentagem.get(1), receita, valorInvestimento);
                return new InvestmentMethodsResponse(user.getInvestimento(), valorInvestimento);
            case "EASY":
                valorInvestimento = receita.multiply(porcentagem.getFirst().divide(new BigDecimal(100))).setScale(0, RoundingMode.CEILING);
                logger.info("Cálculo para perfil EASY: {}% de {} = {}", porcentagem.getFirst(), receita, valorInvestimento);
                return new InvestmentMethodsResponse(user.getInvestimento(), valorInvestimento);
            default:
                logger.error("Tipo de investimento inválido encontrado para o usuário {}: {}", userDetails.getUsername(), investimentoENUM);
                throw new IllegalArgumentException("Não foi possível calcular seu investimento");
        }
    }
}
