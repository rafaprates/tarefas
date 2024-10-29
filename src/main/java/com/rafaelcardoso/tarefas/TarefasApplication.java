package com.rafaelcardoso.tarefas;

import com.rafaelcardoso.tarefas.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableCaching
@RequiredArgsConstructor
public class TarefasApplication implements CommandLineRunner {

    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(TarefasApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userService.criar("usuario001@email.com", "usuario001");
        userService.criar("usuario002@email.com", "usuario002");
    }
}
