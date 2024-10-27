package com.rafaelcardoso.tarefas.auth.controller;

import com.rafaelcardoso.tarefas.auth.dto.LoginResponse;
import com.rafaelcardoso.tarefas.auth.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Autenticação")
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "Autentica usuário e gera um token para acesso")
    public ResponseEntity<LoginResponse> token(Authentication authentication) {
        String token = tokenService.gerarToken(authentication);

        LoginResponse loginResponse = new LoginResponse(token);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
