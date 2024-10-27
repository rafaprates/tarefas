package com.rafaelcardoso.tarefas.tarefa.mapper;

import com.rafaelcardoso.tarefas.tarefa.dto.TarefaResponse;
import com.rafaelcardoso.tarefas.tarefa.entidade.Tarefa;
import org.springframework.stereotype.Service;

@Service
public class TarefaMapper {
    public TarefaResponse toResponse(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getCriadaPor().getUsername(),
                tarefa.getCriadaEm(),
                tarefa.getEstado()
        );
    }
}
