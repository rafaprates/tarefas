package com.rafaelcardoso.tarefas.comum.exception;

import com.rafaelcardoso.tarefas.comum.dto.ErroResponse;
import com.rafaelcardoso.tarefas.tarefa.exception.TarefaNaoEncontradaEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErroController {

    @ExceptionHandler(TarefaNaoEncontradaEncontradaException.class)
    public ResponseEntity<ErroResponse> tarefaNaoEncontrada() {
        return new ResponseEntity<>(new ErroResponse("Tarefa não encontrada"), HttpStatus.NOT_FOUND);
    }
}
