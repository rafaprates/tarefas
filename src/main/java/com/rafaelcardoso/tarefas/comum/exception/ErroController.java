package com.rafaelcardoso.tarefas.comum.exception;

import com.rafaelcardoso.tarefas.comum.dto.ErroResponse;
import com.rafaelcardoso.tarefas.tarefa.exception.PermissaoInsuficienteException;
import com.rafaelcardoso.tarefas.tarefa.exception.TarefaNaoEncontradaEncontradaException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErroController {

    @ExceptionHandler(TarefaNaoEncontradaEncontradaException.class)
    public ResponseEntity<ErroResponse> tarefaNaoEncontrada(TarefaNaoEncontradaEncontradaException e) {
        log.error("Tarefa {} não encontrada por usuário {}", e.getId(), MDC.get("usuario"));
        final String mensagem = String.format("Tarefa %d não encontrada", e.getId());
        return new ResponseEntity<>(new ErroResponse(mensagem), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissaoInsuficienteException.class)
    public ResponseEntity<ErroResponse> permissaoInsuficiente(PermissaoInsuficienteException e) {
        log.error("Usuário {} não tem permissão para alterar/deletar a tarefa {}", e.getUsername(), e.getRecursoId());
        return new ResponseEntity<>(new ErroResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
