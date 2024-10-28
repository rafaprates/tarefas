package com.rafaelcardoso.tarefas.tarefa.service;

import com.rafaelcardoso.tarefas.auth.entidades.Usuario;
import com.rafaelcardoso.tarefas.auth.service.UserService;
import com.rafaelcardoso.tarefas.tarefa.dto.NovaTarefaRequest;
import com.rafaelcardoso.tarefas.tarefa.dto.NovoStatusRequest;
import com.rafaelcardoso.tarefas.tarefa.entidade.Estado;
import com.rafaelcardoso.tarefas.tarefa.entidade.Tarefa;
import com.rafaelcardoso.tarefas.tarefa.exception.PermissaoInsuficienteException;
import com.rafaelcardoso.tarefas.tarefa.exception.TarefaNaoEncontradaEncontradaException;
import com.rafaelcardoso.tarefas.tarefa.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TarefaService tarefaService;

    private CacheManager cacheManager;

    private Usuario usuario01;

    private Usuario usuario02;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cacheManager = new ConcurrentMapCacheManager("tarefas");
        usuario01 = new Usuario("usuario01@email.com", "123456");
        usuario02 = new Usuario("usuario02@email.com", "123456");
    }

    @Test
    @DisplayName("Não deve encontrar tarefa por id quando ela não existe")
    void naoDeveEncontrarTarefaPorId_QuandoElaNaoExiste() throws TarefaNaoEncontradaEncontradaException {
        when(tarefaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradaEncontradaException.class, () ->
                tarefaService.buscarPorId(1L)
        );
    }

    @Test
    @DisplayName("Deve alterar status da tarefa quando ela existe")
    void deveAlterarStatus_QuandoTarefaExiste() throws TarefaNaoEncontradaEncontradaException {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(1L);
        NovoStatusRequest novoStatusRequest = new NovoStatusRequest(Estado.EM_ANDAMENTO);

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

        tarefaService.alterarStatus(1L, novoStatusRequest);

        verify(tarefaRepository, times(1)).save(tarefa);
        assertEquals(Estado.EM_ANDAMENTO, tarefa.getEstado());
    }

    @Test
    @DisplayName("Não deve alterar status da tarefa quando ela não existe")
    void naoDeveAlterarStatus_QuandoTarefaNaoExiste() {
        NovoStatusRequest novoStatusRequest = new NovoStatusRequest(Estado.EM_ANDAMENTO);

        when(tarefaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradaEncontradaException.class, () ->
                tarefaService.alterarStatus(1L, novoStatusRequest)
        );
    }

    @Test
    @DisplayName("Deve atualizar tarefa quando o solicitante é seu dono e ela existe")
    void deveAtualizarTarefa_QuandoSolicitanteEhSeuDono() {
        Tarefa tarefa = new Tarefa("Titulo", "Descricao", usuario01);
        NovaTarefaRequest novaTarefaRequest = new NovaTarefaRequest("Novo Titulo", "Nova Descricao");
        Principal solicitante = mock(Principal.class);

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(solicitante.getName()).thenReturn(usuario01.getUsername());

        Tarefa result = tarefaService.atualizar(solicitante, 1L, novaTarefaRequest);

        verify(tarefaRepository, times(1)).save(tarefa);
    }

    @Test
    @DisplayName("Nao deve atualizar tarefa quando solicitante não é seu dono")
    void naoDeveAtualizarTarefa_QuandoSolicitanteNaoEhSeuDono() {
        Tarefa tarefa = new Tarefa("Titulo", "Descricao", usuario01);
        NovaTarefaRequest novaTarefaRequest = new NovaTarefaRequest("Novo Titulo", "Nova Descricao");
        Principal solicitante = mock(Principal.class);

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(solicitante.getName()).thenReturn(usuario02.getUsername());

        assertThrows(PermissaoInsuficienteException.class, () ->
                tarefaService.atualizar(solicitante, 1L, novaTarefaRequest)
        );
    }

    @Test
    @DisplayName("Não deve atualizar tarefa quandoarefa ela não existe")
    void naoDeveAtualizarTarefa_QuandoTarefaNaoExiste() {
        NovaTarefaRequest novaTarefaRequest = new NovaTarefaRequest("Novo Titulo", "Nova Descricao");
        Principal solicitante = mock(Principal.class);

        when(tarefaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradaEncontradaException.class, () ->
                tarefaService.atualizar(solicitante, 1L, novaTarefaRequest)
        );

        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }

    @Test
    @DisplayName("Deve deletar tarefa quando o solicitante é seu dono")
    void deveDeletarTarefa_QuandoSolicitanteEhSeuDono() {
        Tarefa tarefa = new Tarefa("Titulo", "Descricao", usuario01);
        Principal solicitante = mock(Principal.class);

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(solicitante.getName()).thenReturn(usuario01.getUsername());

        tarefaService.deletar(solicitante, 1L);

        verify(tarefaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Não deve deletar tarefa quando solicitante não é seu dono")
    void naoDeveDeletarTarefa_QuandoSolicitanteNaoEhSeuDono() {
        Tarefa tarefa = new Tarefa("Titulo", "Descricao", usuario01);
        Principal solicitante = mock(Principal.class);

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(solicitante.getName()).thenReturn(usuario02.getUsername());

        assertThrows(PermissaoInsuficienteException.class, () ->
                tarefaService.deletar(solicitante, 1L)
        );
    }
}
