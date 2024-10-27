package com.rafaelcardoso.tarefas.auth.service;

import com.rafaelcardoso.tarefas.auth.entidades.Usuario;
import com.rafaelcardoso.tarefas.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info("Logando usuário {}", email);
        return usuarioRepository.findByEmail(email);
    }

    @Cacheable("usuarios")
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void criar(String email, String senha) {
        Usuario u = new Usuario(email, passwordEncoder.encode(senha));
        log.info("Usuário {} criado", email);
        usuarioRepository.save(u);
    }
}
