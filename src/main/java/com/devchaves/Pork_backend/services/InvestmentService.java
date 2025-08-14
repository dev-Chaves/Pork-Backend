package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.InvestmentMethodsResponse;
import com.devchaves.Pork_backend.DTO.InvestmentRequestDTO;
import com.devchaves.Pork_backend.DTO.InvestmentResponseDTO;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Stream;

@Service
public class InvestmentService {

    private final UserRepository userRepository;

    public InvestmentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final List<BigDecimal> porcentagem = Stream.of(10,30,50).map(BigDecimal::new).toList();

    @Transactional
    public InvestmentResponseDTO selecionarInvestimento(InvestmentRequestDTO dto, UserDetails userDetails){

        UserEntity user = (UserEntity) userDetails;

        userRepository.updateInvestimento(user.getId(), dto.tipo().toString());

        return new InvestmentResponseDTO(dto.tipo().toString());
    }

    public InvestmentMethodsResponse calcularInvestimentos(UserDetails userDetails){

        UserEntity user = (UserEntity) userDetails;

        String InvestimentoENUM = user.getInvestimento().toString();

        return switch (InvestimentoENUM) {
            case "HARD" ->
                    new InvestmentMethodsResponse(
                            user.getInvestimento(),
                            user.getReceita().
                                    multiply(porcentagem.get(2).divide(new BigDecimal(100)))
                                    .setScale(0, RoundingMode.CEILING));

            case "MID" ->
                    new InvestmentMethodsResponse(
                            user.getInvestimento(),
                            user.getReceita()
                                    .multiply(porcentagem.get(1).divide(new BigDecimal(100)))
                                    .setScale(0, RoundingMode.CEILING));

            case "EASY" ->
                    new InvestmentMethodsResponse(user.getInvestimento(),
                            user.getReceita()
                                    .multiply(porcentagem.getFirst().divide(new BigDecimal(100)))
                                    .setScale(0, RoundingMode.CEILING));

            default -> throw new IllegalArgumentException("Não foi possível calcular seu investimento");
        };

    }

}
