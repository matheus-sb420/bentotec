package com.bentotec.sistema_votacao.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final String issuer;
    private final long adminExpirationHours;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.admin-expiration-hours}") long adminExpirationHours) {

        SecretKey key = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        this.encoder = NimbusJwtEncoder
                .withSecretKey(key)
                .algorithm(MacAlgorithm.HS256)
                .build();

        this.issuer = issuer;
        this.adminExpirationHours = adminExpirationHours;
    }

    public String gerarTokenVisitante(
            UUID identidadeUuid,
            LocalDateTime dataFimEvento) {

        Instant agora = Instant.now();

        Instant expiracao = dataFimEvento
                .atZone(ZoneId.systemDefault())
                .toInstant();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(identidadeUuid.toString())
                .issuedAt(agora)
                .expiresAt(expiracao)
                .claim("tipo", "VISITANTE")
                .id(UUID.randomUUID().toString())
                .build();

        return encoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();
    }

    public String gerarTokenAdmin(Long adminId) {

        Instant agora = Instant.now();

        Instant expiracao = agora.plusSeconds(
                adminExpirationHours * 60 * 60
        );

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(adminId.toString())
                .issuedAt(agora)
                .expiresAt(expiracao)
                .claim("tipo", "ADMIN")
                .id(UUID.randomUUID().toString())
                .build();

        return encoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();
    }
}