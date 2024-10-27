package com.rafaelcardoso.tarefas.tarefa.exception;

import lombok.Getter;

@Getter
public class TarefaNaoEncontradaEncontradaException extends RuntimeException {

    private final long id;

    public TarefaNaoEncontradaEncontradaException(long id) {
        super(String.format("Tarefa %d não encontrada", id));
        this.id = id;
    }
}
