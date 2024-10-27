package com.rafaelcardoso.tarefas.tarefa.controller;

import com.rafaelcardoso.tarefas.tarefa.dto.NovaTarefaRequest;
import com.rafaelcardoso.tarefas.tarefa.dto.NovoStatusRequest;
import com.rafaelcardoso.tarefas.tarefa.dto.TarefaResponse;
import com.rafaelcardoso.tarefas.tarefa.mapper.TarefaMapper;
import com.rafaelcardoso.tarefas.tarefa.service.TarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;
    private final TarefaMapper mapper;

    @PostMapping
    public ResponseEntity<Void> criar(Authentication auth, @RequestBody NovaTarefaRequest tarefa) {
        tarefaService.criar(auth, tarefa);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TarefaResponse>> buscarTodas(Pageable pageable) {
        Page<TarefaResponse> tarefas = tarefaService
                .buscarTodas(pageable)
                .map(mapper::toResponse);

        return new ResponseEntity<>(tarefas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponse> buscarPorId(@PathVariable long id) {
        TarefaResponse tarefa = mapper.toResponse(
                tarefaService.buscarPorId(id)
        );

        return new ResponseEntity<>(tarefa, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TarefaResponse> alterarStatus(@PathVariable long id, @RequestBody NovoStatusRequest status) {
        TarefaResponse tarefa = mapper.toResponse(
                tarefaService.alterarStatus(id, status)
        );

        return new ResponseEntity<>(tarefa, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponse> atualizar(@PathVariable long id, @RequestBody NovaTarefaRequest novaTarefaRequest) {
        TarefaResponse tarefa = mapper.toResponse(
                tarefaService.atualizar(id, novaTarefaRequest)
        );

        return new ResponseEntity<>(tarefa, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(@PathVariable long id) {
        tarefaService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
