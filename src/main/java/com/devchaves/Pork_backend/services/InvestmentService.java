package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.InvestmentRequestDTO;
import com.devchaves.Pork_backend.DTO.InvestmentResponseDTO;
import com.devchaves.Pork_backend.entity.UserEntity;
import com.devchaves.Pork_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class InvestmentService {

    private final UserRepository userRepository;

    private final UtilServices utilServices;

    public InvestmentService(UserRepository userRepository, UtilServices utilServices) {
        this.userRepository = userRepository;
        this.utilServices = utilServices;
    }

    public InvestmentResponseDTO selecionarInvestimento(InvestmentRequestDTO dto){

        UserEntity user = utilServices.getCurrentUser();

        user.setInvestimento(dto.tipo());

        userRepository.save(user);

        return new InvestmentResponseDTO(user.getInvestimento().toString());
    }

}
