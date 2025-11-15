package com.example.Clops.dto;

import com.example.Clops.entity.ConnectionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Ответ с данными соединения волокон")
public class FiberConnectionResponse {

    @Schema(description = "ID соединения", example = "1")
    private Integer id;

    @Schema(description = "ID кабеля", example = "1")
    private Integer cableId;

    @Schema(description = "Название кабеля", example = "Магистральный кабель №1")
    private String cableName;

    @Schema(description = "Исходное волокно", example = "1")
    private Integer fromFiber;

    @Schema(description = "ID целевого объекта", example = "2")
    private Integer toObjectId;

    @Schema(description = "Название целевого объекта", example = "Опорный узел №1")
    private String toObjectName;

    @Schema(description = "Тип целевого объекта", example = "NODE")
    private String toObjectType;

    @Schema(description = "Целевое волокно", example = "1")
    private Integer toFiber;

    @Schema(description = "Статус соединения", example = "CONNECTED")
    private ConnectionStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Дата создания", example = "2024-01-15 10:30:00")
    private LocalDateTime createdAt;
}