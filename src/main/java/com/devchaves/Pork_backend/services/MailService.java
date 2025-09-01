package com.devchaves.Pork_backend.services;

import com.devchaves.Pork_backend.DTO.EmailDTO;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendEmailToRegister(EmailDTO dto){

        try{
            var mensagem = new SimpleMailMessage();

            mensagem.setFrom(senderEmail);
            mensagem.setTo(dto.para());
            mensagem.setSubject(dto.sobre());
            mensagem.setText(dto.corpo());
    
            mailSender.send(mensagem);
            return CompletableFuture.completedFuture(null);
        }catch (Exception e) {
            CompletableFuture<Void> cf = new CompletableFuture<>();
            cf.completeExceptionally(e);
            return cf;
        }
        
        
    }



}
