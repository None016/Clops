package com.example.Clops.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для создания или обновления пользователя")
public class UserRequest {

    @Schema(description = "Имя пользователя", example = "johndoe", minLength = 3, maxLength = 255)
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
    private String username;

    @Schema(description = "Хэш пароля", example = "hashedpassword123", minLength = 1, maxLength = 255)
    @NotBlank(message = "Password hash cannot be blank")
    @Size(min = 1, max = 255, message = "Password hash must be between 1 and 255 characters")
    private String passwordHash;

    @Schema(description = "Активен ли пользователь", example = "true", defaultValue = "true")
    private Boolean isActive = true;
}

