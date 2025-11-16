package com.example.Clops.dto;

import com.example.Clops.entity.LinkType;
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
@Schema(description = "Ответ с данными связи между объектами")
public class ObjectLinkResponse {

    @Schema(description = "ID связи", example = "1")
    private Integer id;

    @Schema(description = "ID исходного объекта", example = "1")
    private Integer fromObjectId;

    @Schema(description = "Название исходного объекта", example = "Опорный узел №1")
    private String fromObjectName;

    @Schema(description = "Тип исходного объекта", example = "node")
    private String fromObjectType;

    @Schema(description = "ID целевого объекта", example = "2")
    private Integer toObjectId;

    @Schema(description = "Название целевого объекта", example = "Муфта №1")
    private String toObjectName;

    @Schema(description = "Тип целевого объекта", example = "SPLICE_CLOSURE")
    private String toObjectType;

    @Schema(description = "Тип связи", example = "OPTICAL")
    private LinkType linkType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Дата создания", example = "2024-01-15 10:30:00")
    private LocalDateTime createdAt;
}
