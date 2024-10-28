package com.rafaelcardoso.tarefas.tarefa.dto;

import com.rafaelcardoso.tarefas.tarefa.entidade.Estado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NovoStatusRequest {
    private Estado estado;
}
