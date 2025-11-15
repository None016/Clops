package com.example.Clops.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с данными территории")
public class TerritoryResponse {

    @Schema(description = "ID территории", example = "1")
    private Integer id;

    @Schema(description = "Название территории", example = "Центральный район")
    private String name;

    @Schema(description = "Описание территории", example = "Центральная часть города с исторической застройкой")
    private String description;
}
