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
@Schema(description = "Запрос для смены пароля")
public class ChangePasswordRequest {

    @Schema(description = "Старый пароль", example = "oldpassword123", required = true)
    @NotBlank(message = "Old password cannot be blank")
    private String oldPassword;

    @Schema(description = "Новый пароль", example = "newpassword456", required = true)
    @NotBlank(message = "New password cannot be blank")
    @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
    private String newPassword;
}
