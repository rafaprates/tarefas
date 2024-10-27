package com.rafaelcardoso.tarefas.tarefa.repository;

import com.rafaelcardoso.tarefas.tarefa.entidades.Estado;
import com.rafaelcardoso.tarefas.tarefa.entidades.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    Page<Tarefa> findAllByEstado(Estado estado, Pageable pageable);
}
