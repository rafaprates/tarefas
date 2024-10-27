package com.rafaelcardoso.tarefas.tarefa.dto;

import com.rafaelcardoso.tarefas.tarefa.entidades.Estado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TarefaResponse {
    private long id;
    private String titulo;
    private String descricao;
    private String criadaPor;
    private LocalDateTime criadaEm;
    private Estado estado;
}
