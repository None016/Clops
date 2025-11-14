package com.example.Clops.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с данными пользователя")
public class UserResponse {

    @Schema(description = "ID пользователя", example = "1")
    private Integer id;

    @Schema(description = "Имя пользователя", example = "johndoe")
    private String username;

    @Schema(description = "Активен ли пользователь", example = "true")
    private Boolean isActive;
}