package com.moneyminder.moneyminderusers.controllers;

import com.moneyminder.moneyminderusers.dto.UserResponseDto;
import com.moneyminder.moneyminderusers.dto.UserUpdateRequestDto;
import com.moneyminder.moneyminderusers.models.User;
import com.moneyminder.moneyminderusers.processors.user.DeleteUserProcessor;
import com.moneyminder.moneyminderusers.processors.user.RetrieveUserProcessor;
import com.moneyminder.moneyminderusers.processors.user.SaveUserProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Controlador para gestión de cuentas de usuario")
public class UserController {
    private final RetrieveUserProcessor retrieveUserProcessor;
    private final SaveUserProcessor saveUserProcessor;
    private final DeleteUserProcessor deleteUserProcessor;

    @GetMapping()
    @Operation(
            summary = "Obtiene todos los usuarios",
            description = "Devuelve la lista completa de usuarios registrados"
    )
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(this.retrieveUserProcessor.retrieveUsers());
    }

    @GetMapping("/{username}")
    @Operation(
            summary = "Obtiene un usuario",
            description = "Devuelve los datos públicos de un usuario dado su nombre de usuario o email"
    )
    public ResponseEntity<UserResponseDto> getUser(
            @Parameter(description = "Nombre de usuario o correo electrónico")
            @PathVariable final String username
    ) {
        return ResponseEntity.ok(this.retrieveUserProcessor.retrieveUserByUsernameOrEmail(username));
    }

    @PostMapping("/new-user")
    @Operation(
            summary = "Crea un nuevo usuario",
            description = "Registra un nuevo usuario y devuelve su token de autenticación"
    )
    public ResponseEntity<?> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo usuario",
                    required = true)
            @Valid @RequestBody final User user
    ) {
        return ResponseEntity.ok(Map.of("accessToken", this.saveUserProcessor.saveUser(user)));
    }

    @PutMapping()
    @Operation(
            summary = "Actualiza un usuario",
            description = "Modifica los datos generales del usuario"
    )
    public ResponseEntity<User> updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del usuario",
                    required = true
            )
            @Valid @RequestBody final User user
    ) {
        return ResponseEntity.ok(this.saveUserProcessor.updateUser(user));
    }

    @PutMapping("/update-user-data")
    @Operation(
            summary = "Actualiza los datos sensibles del usuario",
            description = "Permite actualizar el correo o contraseña del usuario"
    )
    public ResponseEntity<UserResponseDto> updateUserData(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del usuario",
                    required = true
            )
            @Valid @RequestBody final UserUpdateRequestDto user
    ) {
        return ResponseEntity.ok(this.saveUserProcessor.updateUserData(user));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un usuario",
            description = "Elimina un usuario del sistema dado su ID"
    )
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID del usuario a eliminar")
            @PathVariable final String id
    ) {
        this.deleteUserProcessor.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
