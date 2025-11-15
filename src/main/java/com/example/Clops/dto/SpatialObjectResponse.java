package com.example.Clops.dto;

import com.example.Clops.entity.SpatialObjectType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Ответ с данными пространственного объекта")
public class SpatialObjectResponse {

    @Schema(description = "ID объекта", example = "1")
    private Integer id;

    @Schema(description = "Тип объекта", example = "NODE")
    private SpatialObjectType type;

    @Schema(description = "Название объекта", example = "Опорный узел №1")
    private String name;

    @Schema(description = "Описание объекта", example = "Основной опорный узел связи")
    private String description;

    @Schema(description = "Геометрия в WKT формате", example = "POINT(37.6175 55.7558)")
    private String geometryWkt; // Изменили на geometryWkt

    @Schema(description = "Дополнительные атрибуты",
            example = "{\"voltage\": \"220V\", \"status\": \"active\", \"capacity\": 1000}")
    private Map<String, Object> attributes;

    @Schema(description = "ID территории", example = "1")
    private Integer territoryId;

    @Schema(description = "Название территории", example = "Центральный район")
    private String territoryName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Дата создания", example = "2024-01-15 10:30:00")
    private LocalDateTime createdAt;
}