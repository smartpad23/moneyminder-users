package com.moneyminder.moneyminderusers.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotBlank(message = "El usuario debe tener un nombre de usuario")
    @Schema(description = "Nombre de usuario", example = "user1")
    private String username;

    @Email(message = "El usuario debe tener un email con el formato correcto")
    @Schema(description = "Correo electrónico del usuario", example = "user@example.com")
    private String email;

    @NotBlank(message = "El usuario debe tener tener una contraseña")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    @Schema(description = "Lista de IDs de grupos a los que pertenece el usuario")
    private List<String> groups;

    @Schema(description = "Lista de IDs de sesiones activas del usuario")
    private List<String> sessions;

    @Schema(description = "Solicitudes de grupos enviadas por el usuario")
    private List<String> requestingGroups;

    @Schema(description = "Solicitudes de grupos recibidas por el usuario")
    private List<String> requestedGroups;

}
