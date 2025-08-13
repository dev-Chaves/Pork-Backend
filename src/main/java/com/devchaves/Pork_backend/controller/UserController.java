package com.devchaves.Pork_backend.controller;

import com.devchaves.Pork_backend.DTO.UserInfoResponse;
import com.devchaves.Pork_backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuario")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("info")
    public ResponseEntity<UserInfoResponse> consultarInfo(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(userService.consultarInfo(userDetails));
    }
}
