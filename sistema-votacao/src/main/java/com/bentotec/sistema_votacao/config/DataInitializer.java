package com.bentotec.sistema_votacao.config;

import com.bentotec.sistema_votacao.model.Admin;
import com.bentotec.sistema_votacao.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner criarAdminInicial(
            AdminRepository repository,
            PasswordEncoder encoder,
            @Value("${app.admin.email}") String email,
            @Value("${app.admin.password}") String password) {

        return args -> {

            if (repository.findByEmail(email).isEmpty()) {

                Admin admin = new Admin();

                admin.setEmail(email);
                admin.setSenhaHash(
                        encoder.encode(password)
                );

                repository.save(admin);

                System.out.println(
                        "Administrador inicial criado: " + email
                );
            }
        };
    }
}