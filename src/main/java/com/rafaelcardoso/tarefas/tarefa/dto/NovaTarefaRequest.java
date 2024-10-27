package com.rafaelcardoso.tarefas.tarefa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NovaTarefaRequest {
    private String titulo;
    private String descricao;
}