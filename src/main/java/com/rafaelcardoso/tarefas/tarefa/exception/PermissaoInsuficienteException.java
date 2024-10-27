package com.rafaelcardoso.tarefas.tarefa.exception;

public class PermissaoInsuficienteException extends RuntimeException {
    public PermissaoInsuficienteException() {
        super("Permissão insuficiente para realizar a operação");
    }

    public PermissaoInsuficienteException(String message) {
        super(message);
    }
}
