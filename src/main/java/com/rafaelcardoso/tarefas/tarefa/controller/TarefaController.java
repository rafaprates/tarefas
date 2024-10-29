package com.rafaelcardoso.tarefas.tarefa.controller;

import com.rafaelcardoso.tarefas.tarefa.dto.NovaTarefaRequest;
import com.rafaelcardoso.tarefas.tarefa.dto.NovoStatusRequest;
import com.rafaelcardoso.tarefas.tarefa.dto.TarefaResponse;
import com.rafaelcardoso.tarefas.tarefa.entidade.Estado;
import com.rafaelcardoso.tarefas.tarefa.mapper.TarefaMapper;
import com.rafaelcardoso.tarefas.tarefa.service.TarefaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@Api(tags = "Tarefas")
@RequestMapping("/v1/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;
    private final TarefaMapper mapper;

    @PostMapping
    @ApiOperation(value = "Cria uma nova tarefa")
    public ResponseEntity<TarefaResponse> criar(Authentication solicitante, @Valid @RequestBody NovaTarefaRequest tarefa) {
        TarefaResponse criada = mapper.toResponse(
                tarefaService.criar(solicitante, tarefa)
        );

        return new ResponseEntity<>(criada, HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "Busca todas as tarefas de maneira paginada")
    public ResponseEntity<Page<TarefaResponse>> buscarTodas(@RequestParam(required = false) Optional<Estado> status,
                                                            Pageable pageable) {
        Page<TarefaResponse> tarefas = tarefaService
                .buscarTodas(status, pageable)
                .map(mapper::toResponse);

        return new ResponseEntity<>(tarefas, HttpStatus.OK);
    }

    @GetMapping("/{tarefaId}")
    @ApiOperation(value = "Busca uma tarefa por Id")
    public ResponseEntity<TarefaResponse> buscarPorId(@PathVariable long tarefaId) {
        TarefaResponse tarefa = mapper.toResponse(
                tarefaService.buscarPorId(tarefaId)
        );

        return new ResponseEntity<>(tarefa, HttpStatus.OK);
    }

    @PatchMapping("/{tarefaId}/status")
    @ApiOperation(value = "Altera o status de uma tarefa")
    public ResponseEntity<TarefaResponse> alterarStatus(@PathVariable long tarefaId,
                                                        @RequestBody NovoStatusRequest status) {
        TarefaResponse tarefa = mapper.toResponse(
                tarefaService.alterarStatus(tarefaId, status)
        );

        return new ResponseEntity<>(tarefa, HttpStatus.OK);
    }

    @PutMapping("/{tarefaId}")
    @ApiOperation(value = "Atualiza uma tarefa")
    public ResponseEntity<TarefaResponse> atualizar(Principal solicitante,
                                                    @PathVariable long tarefaId,
                                                    @RequestBody NovaTarefaRequest novaTarefaRequest) {
        TarefaResponse tarefa = mapper.toResponse(
                tarefaService.atualizar(solicitante, tarefaId, novaTarefaRequest)
        );

        return new ResponseEntity<>(tarefa, HttpStatus.OK);
    }

    @DeleteMapping("/{tarefaId}")
    @ApiOperation(value = "Deleta uma tarefa")
    public ResponseEntity<Void> deletar(Principal solicitante, @PathVariable long tarefaId) {
        tarefaService.deletar(solicitante, tarefaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
