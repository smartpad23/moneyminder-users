package com.moneyminder.moneyminderusers.models;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class Group {

    @Schema(description = "ID único del grupo", example = "178f3c7a-4ad6-4dc7-b338-ca507dfb8f5e")
    private String id;

    @NotBlank(message = "El grupo debe tener un nombre")
    @Schema(description = "Nombre del grupo", example = "Grupo de amigos")
    private String name;

    @Schema(description = "Lista de IDs de usuario que pertenecen al grupo")
    private List<String> users;

    @Schema(description = "Lista de IDs de solicitudes de grupo asociadas")
    private List<String> groupRequests;

}
