package com.moneyminder.moneyminderusers.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest {

    @Schema(description = "ID único de la solicitud", example = "e6a3776f-b29a-4a66-b500-c599e29e6dbe")
    private String id;

    @Schema(description = "ID del grupo al que se solicita unirse", example = "6b4b4c93-6887-4dd3-b576-aa1f687a0237")
    private String group;

    @NotBlank(message = "La solicitud debe estar asociada a un solicitante")
    @Schema(description = "Nombre de usuario del solicitante", example = "user1")
    private String requestingUser;

    @NotBlank(message = "La solicitud debe estar asociada a un destinatario")
    @Schema(description = "Nombre de usuario del solicitado", example = "user2")
    private String requestedUser;

    @Schema(description = "Fecha de la solicitud", type = "string", format = "date", example = "2025-05-10")
    private LocalDate date;

    @Schema(description = "Indica si la solicitud fue aceptada", example = "true")
    private Boolean accepted;

}
