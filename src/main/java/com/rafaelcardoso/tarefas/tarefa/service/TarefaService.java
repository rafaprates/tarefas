package com.rafaelcardoso.tarefas.tarefa.service;

import com.rafaelcardoso.tarefas.auth.entidades.Usuario;
import com.rafaelcardoso.tarefas.auth.service.UserService;
import com.rafaelcardoso.tarefas.tarefa.dto.NovaTarefaRequest;
import com.rafaelcardoso.tarefas.tarefa.dto.NovoStatusRequest;
import com.rafaelcardoso.tarefas.tarefa.entidades.Estado;
import com.rafaelcardoso.tarefas.tarefa.entidades.Tarefa;
import com.rafaelcardoso.tarefas.tarefa.exception.PermissaoInsuficienteException;
import com.rafaelcardoso.tarefas.tarefa.exception.TarefaNaoEncontradaEncontradaException;
import com.rafaelcardoso.tarefas.tarefa.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UserService userService;

    public void criar(Authentication auth, NovaTarefaRequest novaTarefaRequest) {
        Usuario usuario = userService.findByEmail(auth.getName());
        Tarefa tarefa = new Tarefa(novaTarefaRequest.getTitulo(), novaTarefaRequest.getDescricao(), usuario);

        tarefaRepository.save(tarefa);
    }

    @Cacheable("tarefas")
    public Page<Tarefa> buscarTodas(Optional<Estado> status, Pageable pageable) {
        if (status.isPresent())
            return tarefaRepository.findAllByEstado(status.get(), pageable);

        return tarefaRepository.findAll(pageable);
    }

    public Tarefa buscarPorId(long id) throws TarefaNaoEncontradaEncontradaException {
        return tarefaRepository
                .findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaEncontradaException(id));
    }

    @CacheEvict(value = "tarefas", allEntries = true)
    public Tarefa alterarStatus(long id, NovoStatusRequest novoStatusRequest) {
        Tarefa tarefa = this.buscarPorId(id);

        tarefa.alterarEstado(novoStatusRequest.getEstado());

        return tarefaRepository.save(tarefa);
    }

    @CacheEvict(value = "tarefas", allEntries = true)
    public Tarefa atualizar(Principal solicitante, long tarefaId, NovaTarefaRequest novaTarefaRequest) {
        Tarefa tarefa = this.buscarPorId(tarefaId);

        if (!podeAtualizarOuExcluir(solicitante, tarefa.getCriadaPor()))
            throw new PermissaoInsuficienteException(solicitante.getName(), "Você não tem permissão para atualizar essa tarefa");


        tarefa.atualizar(novaTarefaRequest.getTitulo(), novaTarefaRequest.getDescricao());

        return tarefaRepository.save(tarefa);
    }

    @CacheEvict(value = "tarefas", allEntries = true)
    public void deletar(Principal solicitante, long tarefaId) {
        Tarefa tarefa = this.buscarPorId(tarefaId);

        if (!podeAtualizarOuExcluir(solicitante, tarefa.getCriadaPor()))
            throw new PermissaoInsuficienteException(solicitante.getName(), "Você não tem permissão para excluir essa tarefa");


        tarefaRepository.deleteById(tarefaId);
    }

    private boolean podeAtualizarOuExcluir(Principal solicitante, Usuario donoTarefa) {
        return solicitante.getName().equals(donoTarefa.getUsername());
    }
}
