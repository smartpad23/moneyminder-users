package com.moneyminder.moneyminderusers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {

    @Schema(description = "Nombre de usuario", example = "user1")
    private String username;

    @Email(message = "El usuario debe tener un email con el formato correcto")
    @Schema(description = "Nuevo correo electrónico del usuario", example = "user@email.com")
    private String email;

    @Schema(description = "Contraseña actual", example = "oldPass123")
    private String oldPassword;

    @Schema(description = "Nueva contraseña", example = "newPass456")
    private String newPassword;

}
