package com.moneyminder.moneyminderusers.controllers;

import com.moneyminder.moneyminderusers.models.Session;
import com.moneyminder.moneyminderusers.processors.session.DeleteSessionProcessor;
import com.moneyminder.moneyminderusers.processors.session.RetrieveSessionProcessor;
import com.moneyminder.moneyminderusers.processors.session.SaveSessionProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/session")
@RestController
@RequiredArgsConstructor
@Tag(name = "Sesiones", description = "Gestión de sesiones activas de usuarios")
public class SessionController {
    private final RetrieveSessionProcessor retrieveSessionProcessor;
    private final SaveSessionProcessor saveSessionProcessor;
    private final DeleteSessionProcessor deleteSessionProcessor;

    @GetMapping()
    @Operation(
            summary = "Obtiene todas las sesiones",
            description = "Devuelve la lista de todas las sesiones registradas"
    )
    public ResponseEntity<List<Session>> getSessions() {
        return ResponseEntity.ok(this.retrieveSessionProcessor.retrieveSessions());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene una sesión por ID",
            description = "Devuelve una sesión específica dado su ID"
    )
    public ResponseEntity<Session> getSession(
            @Parameter(description = "ID de la sesión")
            @PathVariable final String id
    ) {
        return ResponseEntity.ok(this.retrieveSessionProcessor.retrieveSessionById(id));
    }

    @PostMapping()
    @Operation(
            summary = "Crea una nueva sesión",
            description = "Registra una nueva sesión en el sistema"
    )
    public ResponseEntity<Session> createSession(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la sesión a crear",
                    required = true
            )
            @Valid @RequestBody final Session session
    ) {
        return ResponseEntity.ok(this.saveSessionProcessor.saveSession(session));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina una sesión",
            description = "Elimina una sesión existente dado su ID"
    )
    public ResponseEntity<Void> deleteSession(
            @Parameter(description = "ID de la sesión a eliminar")
            @PathVariable final String id
    ) {
        this.deleteSessionProcessor.deleteSession(id);
        return ResponseEntity.ok().build();
    }
}
