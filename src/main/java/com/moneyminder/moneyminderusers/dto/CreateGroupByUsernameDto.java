package com.moneyminder.moneyminderusers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupByUsernameDto {

    @NotBlank(message = "Se debe indicar el nombre de usuario para crear el grupo")
    @Schema(description = "Nombre de usuario que crea el grupo", example = "user1")
    private String username;

    @NotBlank(message = "Se debe indicar el nombre del grupo a crear")
    @Schema(description = "Nombre del grupo a crear", example = "Nuevo grupo")
    private String groupName;

}
