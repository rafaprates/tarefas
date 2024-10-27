package com.rafaelcardoso.tarefas.tarefa.exception;

public class TarefaNaoEncontradaEncontradaException extends RuntimeException {

    public TarefaNaoEncontradaEncontradaException(String message) {
        super(message);
    }

    public TarefaNaoEncontradaEncontradaException() {
        super("Tarefa não encontrada");
    }
}
