package com.rafaelcardoso.tarefas.tarefa.exception;

import lombok.Getter;

@Getter
public class PermissaoInsuficienteException extends RuntimeException {
    private final String username;

    public PermissaoInsuficienteException(String username) {
        super("Permissão insuficiente para realizar a operação");
        this.username = username;
    }

    public PermissaoInsuficienteException(String username, String message) {
        super(message);
        this.username = username;
    }
}
