package com.rafaelcardoso.tarefas.comum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErroResponse {
    private String mensagem;

    public ErroResponse(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
