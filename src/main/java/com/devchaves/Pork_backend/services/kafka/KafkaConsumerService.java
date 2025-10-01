package com.devchaves.Pork_backend.services.kafka;

import com.devchaves.Pork_backend.DTO.EmailDTO;
import com.devchaves.Pork_backend.DTO.ExpenseRequestDTO;
import com.devchaves.Pork_backend.services.ExpensesService;
import com.devchaves.Pork_backend.services.MailService;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumerService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(KafkaConsumerService.class);

    private final MailService mailService;

    private final ExpensesService expensesService;

    public KafkaConsumerService(MailService mailService, ExpensesService expensesService) {
        this.mailService = mailService;
        this.expensesService = expensesService;
    }

    @KafkaListener(topics = "email-topic", groupId = "pork-group" )
    public void consumeEmailEvent(EmailDTO dto){

        logger.info("Evento de email recebido do Kafka para {}", dto.para());
        try{
            // Join para esperar o envio do email ser concluído antes de continuar / tipo await
            mailService.sendEmail(dto).join();
            logger.info("Email enviado para {}", dto.para());
        }catch (Exception e){
            logger.error("Erro ao processar o evento de email do Kafka: {}", e.getMessage());
        }

    }

    @KafkaListener(topics = "income-update-topic", groupId = "pork-group" )
    public void consumeIncomeUpdateEvent(EmailDTO dto){
        logger.info("Evento de atualização de renda recebido do Kafka: {}", dto.para());
        try {
            mailService.sendEmail(dto).join();
        }catch (Exception e){
            logger.info("Erro ao processar o evento de atualização de renda do Kafka: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar o evento de atualização de renda do Kafka");
        }
    }

    @KafkaListener(topics = "despesas-topic", groupId = "pork-group")
    public void consumerDespesasEvent(List<ExpenseRequestDTO> dto){

        logger.info("Evento para cadastro de despesas recebido do Kafka");


    }

}
