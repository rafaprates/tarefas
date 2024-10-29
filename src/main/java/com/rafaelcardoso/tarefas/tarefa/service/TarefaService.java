package com.rafaelcardoso.tarefas.tarefa.service;

import com.rafaelcardoso.tarefas.auth.entidades.Usuario;
import com.rafaelcardoso.tarefas.auth.service.UserService;
import com.rafaelcardoso.tarefas.tarefa.dto.NovaTarefaRequest;
import com.rafaelcardoso.tarefas.tarefa.dto.NovoStatusRequest;
import com.rafaelcardoso.tarefas.tarefa.entidade.Estado;
import com.rafaelcardoso.tarefas.tarefa.entidade.Tarefa;
import com.rafaelcardoso.tarefas.tarefa.exception.PermissaoInsuficienteException;
import com.rafaelcardoso.tarefas.tarefa.exception.TarefaNaoEncontradaEncontradaException;
import com.rafaelcardoso.tarefas.tarefa.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UserService userService;

    public Tarefa criar(Authentication auth, NovaTarefaRequest novaTarefaRequest) {
        Usuario usuario = userService.findByEmail(auth.getName());
        Tarefa tarefa = new Tarefa(novaTarefaRequest.getTitulo(), novaTarefaRequest.getDescricao(), usuario);

        return tarefaRepository.save(tarefa);
    }

    public Page<Tarefa> buscarTodas(Optional<Estado> status, Pageable pageable) {
        if (status.isPresent())
            return tarefaRepository.findAllByEstado(status.get(), pageable);

        return tarefaRepository.findAll(pageable);
    }

    // Adiciona a tarefa ao cache para melhorar a performance da aplicação
    @Cacheable(cacheNames = "tarefas", key = "#id")
    public Tarefa buscarPorId(long id) throws TarefaNaoEncontradaEncontradaException {
        return tarefaRepository
                .findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaEncontradaException(id));
    }

    @CachePut(cacheNames = "tarefas", key = "#id") // Atualiza o cache da tarefa
    public Tarefa alterarStatus(long id, NovoStatusRequest novoStatusRequest) {
        Tarefa tarefa = this.buscarPorId(id);

        tarefa.alterarEstado(novoStatusRequest.getEstado());

        log.info("O status da tarefa {} foi alterado para {} por {}", id, novoStatusRequest.getEstado(), MDC.get("usuario"));

        return tarefaRepository.save(tarefa);
    }

    @CachePut(cacheNames = "tarefas", key = "#tarefaId") // Atualiza o cache da tarefa
    public Tarefa atualizar(Principal solicitante, long tarefaId, NovaTarefaRequest novaTarefaRequest) {
        Tarefa tarefa = this.buscarPorId(tarefaId);

        if (!podeAtualizarOuExcluir(solicitante, tarefa.getCriadaPor()))
            throw new PermissaoInsuficienteException(solicitante.getName(), tarefaId, "Você não tem permissão para atualizar essa tarefa");

        tarefa.atualizar(novaTarefaRequest.getTitulo(), novaTarefaRequest.getDescricao());

        log.info("A tarefa {} foi atualizada por {}", tarefaId, MDC.get("usuario"));

        return tarefaRepository.save(tarefa);
    }

    @CacheEvict(cacheNames = "tarefas", key = "#tarefaId") // Remove o cache da tarefa com o id informado
    public void deletar(Principal solicitante, long tarefaId) {
        Tarefa tarefa = this.buscarPorId(tarefaId);

        if (!podeAtualizarOuExcluir(solicitante, tarefa.getCriadaPor()))
            throw new PermissaoInsuficienteException(solicitante.getName(), tarefaId, "Você não tem permissão para excluir essa tarefa");

        log.info("A tarefa {} foi deletada por {}", tarefaId, MDC.get("usuario"));

        tarefaRepository.deleteById(tarefaId);
    }

    private boolean podeAtualizarOuExcluir(Principal solicitante, Usuario donoTarefa) {
        return solicitante.getName().equals(donoTarefa.getUsername());
    }
}
