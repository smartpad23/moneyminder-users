package com.moneyminder.moneyminderusers.controllers;

import com.moneyminder.moneyminderusers.dto.CreateGroupByUsernameDto;
import com.moneyminder.moneyminderusers.models.Group;
import com.moneyminder.moneyminderusers.processors.group.DeleteGroupProcessor;
import com.moneyminder.moneyminderusers.processors.group.RetrieveGroupProcessor;
import com.moneyminder.moneyminderusers.processors.group.SaveGroupProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/group")
@RestController
@RequiredArgsConstructor
@Tag(name = "Grupos", description = "Controlador para operaciones CRUD relacionadas con grupos")
public class GroupController {
    private final RetrieveGroupProcessor retrieveGroupProcessor;
    private final SaveGroupProcessor saveGroupProcessor;
    private final DeleteGroupProcessor deleteGroupProcessor;

    @GetMapping()
    @Operation(
            summary = "Obtiene todos los grupos",
            description = "Devuelve la lista completa de grupos registrados en el sistema"
    )
    public ResponseEntity<List<Group>> getGroups() {
        return ResponseEntity.ok(this.retrieveGroupProcessor.retrieveGroups());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene un grupo por ID",
            description = "Devuelve un grupo específico según su ID"
    )
    public ResponseEntity<Group> getGroup(
            @Parameter(description = "ID del grupo a obtener")
            @PathVariable final String id
    ) {
        return ResponseEntity.ok(this.retrieveGroupProcessor.retrieveGroupById(id));
    }

    @GetMapping("/by-username/{username}")
    @Operation(
            summary = "Obtiene los IDs de grupos por nombre de usuario",
            description = "Devuelve los IDs de los grupos a los que pertenece un usuario"
    )
    public ResponseEntity<List<String>> getGroupIdsByUsername(
            @Parameter(description = "Nombre de usuario")
            @PathVariable String username
    ) {
        return ResponseEntity.ok(this.retrieveGroupProcessor.retrieveGroupIdsByUsername(username));
    }

    @GetMapping("/usernames-of/{groupId}")
    @Operation(
            summary = "Obtiene los nombres de usuario de un grupo",
            description = "Devuelve los nombres de usuario que forman parte del grupo especificado"
    )
    public ResponseEntity<List<String>> getGroupIdsByUsernameOfGroup(@PathVariable String groupId) {
        return ResponseEntity.ok(this.retrieveGroupProcessor.retrieveUsernameOfGroup(groupId));
    }

    @PostMapping()
    @Operation(
            summary = "Crea un nuevo grupo",
            description = "Registra un nuevo grupo en el sistema con los datos proporcionados"
    )
    public ResponseEntity<Group> createGroup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del grupo a crear",
                    required = true
            )
            @Valid @RequestBody final Group group
    ) {
        return ResponseEntity.ok(this.saveGroupProcessor.saveGroup(group));
    }

    @PostMapping("/budget")
    @Operation(
            summary = "Crea un grupo vinculado a un presupuesto",
            description = "Registra un nuevo grupo a partir del nombre de usuario y el nombre del grupo"
    )
    public ResponseEntity<String> createGroupForBudget(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Información necesaria para crear el grupo",
                    required = true
            )
            @Valid @RequestBody final CreateGroupByUsernameDto infoGroup
    ) {
        return ResponseEntity.ok(this.saveGroupProcessor.createGroupByUsername(infoGroup));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza un grupo",
            description = "Modifica los datos de un grupo existente, dado su ID"
    )
    public ResponseEntity<Group> updateGroup(
            @Parameter(description = "ID del grupo a actualizar")
            @PathVariable final String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del grupo",
                    required = true
            )
            @Valid @RequestBody final Group group) {
        return ResponseEntity.ok(this.saveGroupProcessor.updateGroup(id, group));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un grupo",
            description = "Elimina un grupo a partir de su ID"
    )
    public ResponseEntity<Void> deleteGroup(
            @Parameter(description = "ID del grupo a eliminar")
            @PathVariable final String id
    ) {
        this.deleteGroupProcessor.deleteGroup(id);
        return ResponseEntity.ok().build();
    }
}
