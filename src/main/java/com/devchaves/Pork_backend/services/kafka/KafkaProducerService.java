package com.devchaves.Pork_backend.services.kafka;

import com.devchaves.Pork_backend.DTO.EmailDTO;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmailEvent(EmailDTO dto){
        try {
            logger.info("Enviando email para o tópico Kafka: {}", dto.para());
            kafkaTemplate.send("email-topic", dto);
            logger.info("Email enviado para o tópico Kafka: {}", dto.para());
        }catch (Exception e){
            logger.info("Erro ao enviar email para o tópico Kafka: {}", e.getMessage());
            throw new RuntimeException("Erro ao enviar mensagem para o Kafka");
        }
    }

}
