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
public class UserResponseDto {

    @Schema(description = "Nombre de usuario", example = "user1")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "user@email.com")
    private String email;

}
