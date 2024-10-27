package com.rafaelcardoso.tarefas.tarefa.repository;

import com.rafaelcardoso.tarefas.tarefa.entidades.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}
