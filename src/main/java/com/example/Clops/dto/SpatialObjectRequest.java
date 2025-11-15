package com.example.Clops.dto;

import com.example.Clops.entity.SpatialObjectType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для создания или обновления пространственного объекта")
public class SpatialObjectRequest {

    @Schema(description = "Тип объекта", example = "NODE", required = true)
    @NotNull(message = "Type cannot be null")
    private SpatialObjectType type;

    @Schema(description = "Название объекта", example = "Опорный узел №1", minLength = 2, maxLength = 255, required = true)
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

    @Schema(description = "Описание объекта", example = "Основной опорный узел связи")
    private String description;

    @Schema(description = "Геометрия в WKT формате", example = "POINT(37.6175 55.7558)")
    private String geometryWkt; // Изменили на geometryWkt

    @Schema(description = "Дополнительные атрибуты в формате JSON",
            example = "{\"voltage\": \"220V\", \"status\": \"active\", \"capacity\": 1000}")
    private Map<String, Object> attributes;

    @Schema(description = "ID территории", example = "1")
    private Integer territoryId;
}