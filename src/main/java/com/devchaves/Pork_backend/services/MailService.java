package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.EmailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendEmailToRegister(EmailDTO dto){
        logger.info("Preparando para enviar email para: {}", dto.para());
        try{
            var mensagem = new SimpleMailMessage();
            mensagem.setFrom(senderEmail);
            mensagem.setTo(dto.para());
            mensagem.setSubject(dto.sobre());
            mensagem.setText(dto.corpo());
    
            mailSender.send(mensagem);
            logger.info("Email enviado com sucesso para: {}", dto.para());
            return CompletableFuture.completedFuture(null);
        }catch (Exception e) {
            logger.error("Falha ao enviar email para: {}. Erro: {}", dto.para(), e.getMessage());
            CompletableFuture<Void> cf = new CompletableFuture<>();
            cf.completeExceptionally(e);
            return cf;
        }
    }
}
