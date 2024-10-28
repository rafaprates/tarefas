package com.rafaelcardoso.tarefas.tarefa.service;

import com.rafaelcardoso.tarefas.auth.entidades.Usuario;
import com.rafaelcardoso.tarefas.auth.service.UserService;
import com.rafaelcardoso.tarefas.tarefa.entidade.Estado;
import com.rafaelcardoso.tarefas.tarefa.entidade.Tarefa;
import com.rafaelcardoso.tarefas.tarefa.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TarefaServiceIntegracaoTest {

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UserService userService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        tarefaRepository.deleteAll();

        // Criando um usuário para associar as tarefas
        this.usuario = userService.criar("usuario@teste.com.br", "123456");

        // Criando algumas tarefas com diferentes estados para o teste
        Tarefa t1 = new Tarefa("Tarefa 1", "Descricao 1", this.usuario);
        Tarefa t2 = new Tarefa("Tarefa 2", "Descricao 2", this.usuario);
        Tarefa t3 = new Tarefa("Tarefa 3", "Descricao 3", this.usuario);
        Tarefa t4 = new Tarefa("Tarefa 4", "Descricao 4", this.usuario);

        t1.setEstado(Estado.PENDENTE);
        t2.setEstado(Estado.EM_ANDAMENTO);
        t3.setEstado(Estado.CONCLUIDA);
        t4.setEstado(Estado.CONCLUIDA);

        tarefaRepository.save(t1);
        tarefaRepository.save(t2);
        tarefaRepository.save(t3);
        tarefaRepository.save(t4);
    }

    @Test
    @DisplayName("Deve buscar todas as tarefas filtrando por status")
    void testBuscarTodasTarefasFiltrandoPorStatusConcluida() {
        Pageable pageable = PageRequest.of(0, 10);
        Optional<Estado> statusConcluida = Optional.of(Estado.CONCLUIDA);

        Page<Tarefa> result = tarefaService.buscarTodas(statusConcluida, pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(tarefa -> tarefa.getEstado() == Estado.CONCLUIDA);
    }

    @Test
    @DisplayName("Deve buscar todas as tarefas sem filtro de status")
    void testBuscarTodasTarefasSemFiltragemDeStatus() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Tarefa> result = tarefaService.buscarTodas(Optional.empty(), pageable);

        assertThat(result.getContent()).hasSize(4); // Deve retornar todas as tarefas
    }
}