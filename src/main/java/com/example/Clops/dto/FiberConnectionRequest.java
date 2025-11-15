package com.example.Clops.dto;

import com.example.Clops.entity.ConnectionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для создания или обновления соединения волокон")
public class FiberConnectionRequest {

    @Schema(description = "ID кабеля", example = "1", required = true)
    @NotNull(message = "Cable ID cannot be null")
    private Integer cableId;

    @Schema(description = "Исходное волокно", example = "1", required = true)
    @NotNull(message = "From fiber cannot be null")
    private Integer fromFiber;

    @Schema(description = "ID целевого объекта", example = "2", required = true)
    @NotNull(message = "To object ID cannot be null")
    private Integer toObjectId;

    @Schema(description = "Целевое волокно", example = "1", required = true)
    @NotNull(message = "To fiber cannot be null")
    private Integer toFiber;

    @Schema(description = "Статус соединения", example = "CONNECTED", defaultValue = "CONNECTED")
    private ConnectionStatus status = ConnectionStatus.CONNECTED;
}
