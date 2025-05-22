package com.moneyminder.moneyminderusers.controllers;

import com.nimbusds.jose.jwk.JWKSet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "JWT", description = "Controlador público para exponer claves públicas JWT")
public class JwtController {

    private final JWKSet jwkSet;

    @GetMapping("/.well-known/jwks.json")
    @Operation(
            summary = "Obtiene claves públicas JWKS",
            description = "Devuelve el conjunto de claves públicas en formato JSON Web Key Set (JWKS)"
    )
    public Map<String, Object> getJwks() {
        return this.jwkSet.toPublicJWKSet().toJSONObject();
    }
}
