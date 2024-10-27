package com.rafaelcardoso.tarefas.tarefa.dto;

import com.rafaelcardoso.tarefas.tarefa.entidades.Estado;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NovoStatusRequest {
    private Estado estado;
}
