package com.bentotec.sistema_votacao.service;

import com.bentotec.sistema_votacao.dto.LoginRequest;
import com.bentotec.sistema_votacao.dto.LoginResponse;
import com.bentotec.sistema_votacao.exception.ApiException;
import com.bentotec.sistema_votacao.exception.ErrorCode;
import com.bentotec.sistema_votacao.model.Admin;
import com.bentotec.sistema_votacao.repository.AdminRepository;
import com.bentotec.sistema_votacao.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AdminService(
            AdminRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        Admin admin = repository
                .findByEmail(request.email())
                .orElseThrow(() ->
                        new ApiException(
                                ErrorCode.CREDENCIAIS_INVALIDAS,
                                "Email ou senha inválidos."
                        )
                );

        if (!passwordEncoder.matches(
                request.senha(),
                admin.getSenhaHash())) {

            throw new ApiException(
                    ErrorCode.CREDENCIAIS_INVALIDAS,
                    "Email ou senha inválidos."
            );
        }

        String token =
                jwtService.gerarTokenAdmin(admin.getId());

        return new LoginResponse(token);
    }
}