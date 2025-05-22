package com.moneyminder.moneyminderusers.controllers;

import com.moneyminder.moneyminderusers.dto.LoginRequestDto;
import com.moneyminder.moneyminderusers.processors.authServices.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Operaciones relacionadas con login y autenticación JWT")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(
            summary = "Inicio de sesión",
            description = "Permite al usuario autenticarse con sus credenciales y obtener un token JWT si son válidas"
    )
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales de acceso",
                    required = true
            )
            @RequestBody LoginRequestDto request) {
        try {
            final String token = authenticationService.authenticationMethod(request);

            return ResponseEntity.ok(Map.of("accessToken", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado");
        }
    }
}
