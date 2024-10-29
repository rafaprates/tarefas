package com.rafaelcardoso.tarefas.tarefa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NovaTarefaRequest {
    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    private String descricao;
}
