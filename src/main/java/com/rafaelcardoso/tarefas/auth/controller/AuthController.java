package com.rafaelcardoso.tarefas.auth.controller;

import com.rafaelcardoso.tarefas.auth.dto.LoginResponse;
import com.rafaelcardoso.tarefas.auth.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> token(Authentication authentication) {
        String token = tokenService.gerarToken(authentication);

        LoginResponse loginResponse = new LoginResponse(token);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/principal")
    public String buscarUsarioLogado(Principal principal) {
        return principal.getName();
    }
}
