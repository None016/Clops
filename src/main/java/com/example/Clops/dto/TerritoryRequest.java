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
@Schema(description = "Запрос для создания или обновления территории")
public class TerritoryRequest {

    @Schema(description = "Название территории", example = "Центральный район", minLength = 2, maxLength = 255)
    @NotBlank(message = "Territory name cannot be blank")
    @Size(min = 2, max = 255, message = "Territory name must be between 2 and 255 characters")
    private String name;

    @Schema(description = "Описание территории", example = "Центральная часть города с исторической застройкой", maxLength = 255)
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}
