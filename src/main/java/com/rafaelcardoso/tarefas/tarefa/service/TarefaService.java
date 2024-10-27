package com.rafaelcardoso.tarefas.tarefa.service;

import com.rafaelcardoso.tarefas.auth.entidades.Usuario;
import com.rafaelcardoso.tarefas.auth.service.UserService;
import com.rafaelcardoso.tarefas.tarefa.dto.NovaTarefaRequest;
import com.rafaelcardoso.tarefas.tarefa.dto.NovoStatusRequest;
import com.rafaelcardoso.tarefas.tarefa.entidades.Tarefa;
import com.rafaelcardoso.tarefas.tarefa.exception.TarefaNaoEncontradaEncontradaException;
import com.rafaelcardoso.tarefas.tarefa.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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

    public Page<Tarefa> buscarTodas(Pageable pageable) {
        return tarefaRepository
                .findAll(pageable);
    }

    public Tarefa buscarPorId(long id) throws TarefaNaoEncontradaEncontradaException {
        return tarefaRepository
                .findById(id)
                .orElseThrow(TarefaNaoEncontradaEncontradaException::new);
    }

    public Tarefa alterarStatus(long id, NovoStatusRequest novoStatusRequest) {
        Tarefa tarefa = this.buscarPorId(id);

        tarefa.alterarEstado(novoStatusRequest.getEstado());

        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizar(long id, NovaTarefaRequest novaTarefaRequest) {
        Tarefa tarefa = this.buscarPorId(id);

        tarefa.atualizar(novaTarefaRequest.getTitulo(), novaTarefaRequest.getDescricao());

        return tarefaRepository.save(tarefa);
    }

    public void deletar(long id) {
        Tarefa tarefa = this.buscarPorId(id);
        tarefaRepository.deleteById(id);
    }
}
