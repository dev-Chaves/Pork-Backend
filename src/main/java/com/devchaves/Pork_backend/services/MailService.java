package com.devchaves.Pork_backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.devchaves.Pork_backend.DTO.EmailDTO;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendEmailToRegister(EmailDTO dto){

        try{
            var mensagem = new SimpleMailMessage();

            mensagem.setFrom(senderEmail);
            mensagem.setTo(dto.para());
            mensagem.setSubject(dto.sobre());
            mensagem.setText(dto.corpo());
    
            mailSender.send(mensagem);
        }catch(Exception e){
            throw new RuntimeException("Falha ao enviar o email!", e);
        }
        
        
    }



}
