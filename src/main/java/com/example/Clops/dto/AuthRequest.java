package com.example.Clops.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для аутентификации")
public class AuthRequest {

    @Schema(description = "Имя пользователя", example = "johndoe", required = true)
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Schema(description = "Пароль", example = "password123", required = true)
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
