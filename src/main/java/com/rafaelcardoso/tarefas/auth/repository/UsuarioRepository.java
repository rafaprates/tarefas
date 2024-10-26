package com.rafaelcardoso.tarefas.auth.repository;

import com.rafaelcardoso.tarefas.auth.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}
