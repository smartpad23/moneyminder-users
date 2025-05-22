package com.moneyminder.moneyminderusers.controllers;

import com.moneyminder.moneyminderusers.dto.GroupRequestWithBudgetNameDto;
import com.moneyminder.moneyminderusers.models.GroupRequest;
import com.moneyminder.moneyminderusers.processors.groupRequest.DeleteGroupRequestProcessor;
import com.moneyminder.moneyminderusers.processors.groupRequest.RetrieveGroupRequestProcessor;
import com.moneyminder.moneyminderusers.processors.groupRequest.SaveGroupRequestProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/group-request")
@RestController
@RequiredArgsConstructor
@Tag(name = "Solicitudes de grupo", description = "Controlador para la gestión de solicitudes entre usuarios y grupos")
public class GroupRequestController {
    private final RetrieveGroupRequestProcessor retrieveGroupRequestProcessor;
    private final SaveGroupRequestProcessor saveGroupRequestProcessor;
    private final DeleteGroupRequestProcessor deleteGroupRequestProcessor;

    @GetMapping()
    @Operation(
            summary = "Obtiene todas las solicitudes de grupo",
            description = "Devuelve la lista completa de solicitudes de grupo"
    )
    public ResponseEntity<List<GroupRequest>> getGroupRequests() {
        return ResponseEntity.ok(this.retrieveGroupRequestProcessor.retrieveGroupRequest());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene una solicitud por ID",
            description = "Devuelve una solicitud de grupo específica dado su ID"
    )
    public ResponseEntity<GroupRequest> getGroupRequest(
            @Parameter(description = "ID de la solicitud")
            @PathVariable final String id
    ) {
        return ResponseEntity.ok(this.retrieveGroupRequestProcessor.retrieveGroupRequestById(id));
    }

    @GetMapping("/by-username/{username}")
    @Operation(
            summary = "Obtiene solicitudes por nombre de usuario",
            description = "Devuelve todas las solicitudes de grupo donde el usuario es solicitante o destinatario"
    )
    public ResponseEntity<List<GroupRequestWithBudgetNameDto>> getGroupRequestsByUsername(
            @Parameter(description = "Nombre de usuario")
            @PathVariable final String username
    ) {
        return ResponseEntity.ok(this.retrieveGroupRequestProcessor.retrieveGroupRequestWithBudgetNameByUsername(username));
    }

    @PostMapping()
    @Operation(
            summary = "Crea una nueva solicitud de grupo",
            description = "Registra una nueva solicitud para que un usuario se una a un grupo"
    )
    public ResponseEntity<GroupRequestWithBudgetNameDto> createGroupRequest(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la solicitud a crear",
                    required = true
            )
            @Valid @RequestBody final GroupRequest groupRequest
    ) {
        return ResponseEntity.ok(this.saveGroupRequestProcessor.saveGroupRequest(groupRequest));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza una solicitud de grupo",
            description = "Modifica una solicitud existente dada su ID"
    )
    public ResponseEntity<GroupRequestWithBudgetNameDto> updateGroupRequest(
            @Parameter(description = "ID de la solicitud")
            @PathVariable final String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la solicitud actualizada",
                    required = true
            )
            @Valid @RequestBody final GroupRequest groupRequest) {
        return ResponseEntity.ok(this.saveGroupRequestProcessor.updateGroupRequest(id, groupRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina una solicitud de grupo",
            description = "Elimina una solicitud de grupo existente por su ID"
    )
    public ResponseEntity<Void> deleteGroupRequest(
            @Parameter(description = "ID de la solicitud a eliminar")
            @PathVariable final String id
    ) {
        this.deleteGroupRequestProcessor.deleteGroupRequest(id);
        return ResponseEntity.ok().build();
    }
}
