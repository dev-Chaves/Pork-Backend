package com.devchaves.Pork_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("health")
public class HealthController {

    @GetMapping
    public String healthcheck(){
        return "Funcionando rodando na: " + System.getenv("HOSTNAME");
    }

}
