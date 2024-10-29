package com.rafaelcardoso.tarefas.comum.exception;

import com.rafaelcardoso.tarefas.comum.dto.ErroResponse;
import com.rafaelcardoso.tarefas.tarefa.exception.PermissaoInsuficienteException;
import com.rafaelcardoso.tarefas.tarefa.exception.TarefaNaoEncontradaEncontradaException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        log.error("Usuário {} não tem permissão para alterar/deletar a tarefa {}. Erro: {}", e.getUsername(), e.getRecursoId(), e.getMessage());
        return new ResponseEntity<>(new ErroResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException e) {
        String mensagem = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Ops.. ocorreu um erro de validação");
        log.error("Erro de validação: {}", mensagem);
        return new ResponseEntity<>(new ErroResponse(mensagem), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> erroGenerico(Exception e) {
        log.error("Erro inesperado", e);
        return new ResponseEntity<>(new ErroResponse("Ops.. ocorreu um erro inesperado"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
