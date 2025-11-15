package com.example.Clops.controller;

import com.example.Clops.dto.FiberConnectionRequest;
import com.example.Clops.dto.FiberConnectionResponse;
import com.example.Clops.entity.ConnectionStatus;
import com.example.Clops.service.FiberConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fiber-connections")
@RequiredArgsConstructor
@Validated
@Tag(name = "🔌 Fiber Connections Management", description = "CRUD операции для управления соединениями волокон")
@SecurityRequirement(name = "bearer-key")
public class FiberConnectionController {

    private final FiberConnectionService fiberConnectionService;

    @Operation(summary = "Получить все соединения", description = "Возвращает список соединений с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping
    public ResponseEntity<Page<FiberConnectionResponse>> getAllFiberConnections(
            @Parameter(description = "Номер страницы (с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Поле для сортировки", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Направление сортировки (asc/desc)", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FiberConnectionResponse> connections = fiberConnectionService.findAll(pageable);
        return ResponseEntity.ok(connections);
    }

    @Operation(summary = "Получить соединения по кабелю", description = "Возвращает список соединений для указанного кабеля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/cable/{cableId}")
    public ResponseEntity<List<FiberConnectionResponse>> getConnectionsByCable(
            @Parameter(description = "ID кабеля", example = "1", required = true)
            @PathVariable Integer cableId) {

        List<FiberConnectionResponse> connections = fiberConnectionService.findByCableId(cableId);
        return ResponseEntity.ok(connections);
    }

    @Operation(summary = "Получить соединения по целевому объекту", description = "Возвращает список соединений для указанного целевого объекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/object/{objectId}")
    public ResponseEntity<List<FiberConnectionResponse>> getConnectionsByObject(
            @Parameter(description = "ID целевого объекта", example = "2", required = true)
            @PathVariable Integer objectId) {

        List<FiberConnectionResponse> connections = fiberConnectionService.findByToObjectId(objectId);
        return ResponseEntity.ok(connections);
    }

    @Operation(summary = "Получить соединения по статусу", description = "Возвращает список соединений с указанным статусом")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<FiberConnectionResponse>> getConnectionsByStatus(
            @Parameter(description = "Статус соединения", example = "CONNECTED", required = true)
            @PathVariable ConnectionStatus status) {

        List<FiberConnectionResponse> connections = fiberConnectionService.findByStatus(status);
        return ResponseEntity.ok(connections);
    }

    @Operation(summary = "Получить соединение по ID", description = "Возвращает соединение по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Соединение найдено"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Соединение не найдено")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FiberConnectionResponse> getFiberConnectionById(
            @Parameter(description = "ID соединения", example = "1", required = true)
            @PathVariable Integer id) {

        return fiberConnectionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать соединение", description = "Создает новое соединение волокон")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Соединение успешно создано",
                    content = @Content(schema = @Schema(implementation = FiberConnectionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные соединения"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "409", description = "Соединение уже существует")
    })
    @PostMapping
    public ResponseEntity<?> createFiberConnection(
            @Parameter(description = "Данные соединения", required = true)
            @Valid @RequestBody FiberConnectionRequest connectionRequest) {

        try {
            FiberConnectionResponse createdConnection = fiberConnectionService.create(connectionRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdConnection);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Обновить соединение", description = "Полностью обновляет данные соединения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Соединение успешно обновлено"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Соединение не найдено"),
            @ApiResponse(responseCode = "400", description = "Неверные данные соединения")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFiberConnection(
            @Parameter(description = "ID соединения", example = "1", required = true)
            @PathVariable Integer id,

            @Parameter(description = "Новые данные соединения", required = true)
            @Valid @RequestBody FiberConnectionRequest connectionRequest) {

        try {
            Optional<FiberConnectionResponse> updatedConnection = fiberConnectionService.update(id, connectionRequest);
            return updatedConnection.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Удалить соединение", description = "Удаляет соединение по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Соединение успешно удалено"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Соединение не найдено")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiberConnection(
            @Parameter(description = "ID соединения", example = "1", required = true)
            @PathVariable Integer id) {

        boolean deleted = fiberConnectionService.delete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Обновить статус соединения", description = "Обновляет статус соединения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно обновлен"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Соединение не найдено")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<FiberConnectionResponse> updateConnectionStatus(
            @Parameter(description = "ID соединения", example = "1", required = true)
            @PathVariable Integer id,

            @Parameter(description = "Новый статус", example = "DISCONNECTED", required = true)
            @RequestParam ConnectionStatus status) {

        return fiberConnectionService.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить количество соединений по кабелю", description = "Возвращает количество соединений для указанного кабеля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение количества"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/cable/{cableId}/count")
    public ResponseEntity<Long> getConnectionsCountByCable(
            @Parameter(description = "ID кабеля", example = "1", required = true)
            @PathVariable Integer cableId) {

        long count = fiberConnectionService.countByCableId(cableId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Получить соединения по кабелю и волокну", description = "Возвращает соединения для указанного кабеля и волокна")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/cable/{cableId}/fiber/{fiber}")
    public ResponseEntity<List<FiberConnectionResponse>> getConnectionsByCableAndFiber(
            @Parameter(description = "ID кабеля", example = "1", required = true)
            @PathVariable Integer cableId,

            @Parameter(description = "Номер волокна", example = "1", required = true)
            @PathVariable Integer fiber) {

        List<FiberConnectionResponse> connections = fiberConnectionService.findByCableAndFiber(cableId, fiber);
        return ResponseEntity.ok(connections);
    }
}
