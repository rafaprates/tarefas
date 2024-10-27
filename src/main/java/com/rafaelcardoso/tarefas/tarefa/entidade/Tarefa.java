package com.rafaelcardoso.tarefas.tarefa.entidade;

import com.rafaelcardoso.tarefas.auth.entidades.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "tarefas")
@Getter
@Setter
public class Tarefa {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String titulo;

    private String descricao;

    @NotNull
    @ManyToOne
    private Usuario criadaPor;

    private LocalDateTime criadaEm;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Estado estado;

    public Tarefa(String titulo, String descricao, Usuario criadaPor) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.criadaPor = criadaPor;
        this.criadaEm = LocalDateTime.now();
        this.estado = Estado.PENDENTE;
    }

    public void iniciar() {
        this.estado = Estado.EM_ANDAMENTO;
    }

    public void atualizar(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public void concluir() {
        this.estado = Estado.CONCLUIDA;
    }

    public void alterarEstado(Estado estado) {
        this.estado = estado;
    }
}
