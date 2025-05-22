package com.moneyminder.moneyminderusers.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    @Schema(description = "ID único de la sesión", example = "4f3724c5-c532-40e3-8d81-bd0218de4264")
    private String id;

    @NotBlank(message = "El token no debe ser nullo")
    @Schema(description = "Token de autenticación", example = "abc123token")
    private String token;

    @Schema(description = "Fecha de creación del token", type = "string", format = "date", example = "2025-05-01")
    private LocalDate creationDate;

    @NotNull(message = "El token debe tener una fecha de caducidad")
    @Schema(description = "Fecha de expiración del token", type = "string", format = "date", example = "2025-05-31")
    private LocalDate expirationDate;

    @NotBlank(message = "El token debe estar asociado a un usuario")
    @Schema(description = "Nombre de usuario asociado al token", example = "user3")
    private String user;

}
