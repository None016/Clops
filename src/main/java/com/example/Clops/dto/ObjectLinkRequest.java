package com.example.Clops.dto;

import com.example.Clops.entity.LinkType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для создания или обновления связи между объектами")
public class ObjectLinkRequest {

    @Schema(description = "ID исходного объекта", example = "1", required = true)
    @NotNull(message = "From object ID cannot be null")
    private Integer fromObjectId;

    @Schema(description = "ID целевого объекта", example = "2", required = true)
    @NotNull(message = "To object ID cannot be null")
    private Integer toObjectId;

    @Schema(description = "Тип связи", example = "OPTICAL", required = true)
    @NotNull(message = "Link type cannot be null")
    private LinkType linkType;
}
