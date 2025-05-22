package com.moneyminder.moneyminderusers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @Schema(description = "Nombre de usuario", example = "user1")
    private String username;

    @Schema(description = "Contraseña del usuario", example = "pass32")
    private String password;
}
