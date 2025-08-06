package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.InvestmentMethodsResponse;
import com.devchaves.Pork_backend.DTO.InvestmentRequestDTO;
import com.devchaves.Pork_backend.DTO.InvestmentResponseDTO;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@Service
public class InvestmentService {

    private final UserRepository userRepository;

    private final UtilServices utilServices;

    public InvestmentService(UserRepository userRepository, UtilServices utilServices) {
        this.userRepository = userRepository;
        this.utilServices = utilServices;
    }

    private static final List<BigDecimal> porcentagem = Stream.of(10,30,50).map(BigDecimal::new).toList();

    @Transactional
    public InvestmentResponseDTO selecionarInvestimento(InvestmentRequestDTO dto){

        Long userId = utilServices.getCurrentUserId();

        userRepository.updateInvestimento(userId, dto.tipo().toString());

        return new InvestmentResponseDTO(dto.tipo().toString());
    }

    public InvestmentMethodsResponse calcularInvestimentos(){

        UserEntity user = utilServices.getCurrentUser();

        String InvestimentoENUM = user.getInvestimento().toString();

        return switch (InvestimentoENUM) {
            case "HARD" ->
                    new InvestmentMethodsResponse(
                            user.getInvestimento(),
                            user.getReceita().
                                    multiply(porcentagem.get(2).divide(new BigDecimal(100))));

            case "MID" ->
                    new InvestmentMethodsResponse(
                            user.getInvestimento(),
                            user.getReceita()
                                    .multiply(porcentagem.get(1).divide(new BigDecimal(100))));

            case "EASY" ->
                    new InvestmentMethodsResponse(user.getInvestimento(),
                            user.getReceita()
                                    .multiply(porcentagem.getFirst().divide(new BigDecimal(100))));

            default -> throw new IllegalArgumentException("Não foi possível calcular seu investimento");
        };

    }

}
