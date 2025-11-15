package com.example.Clops.controller;

import com.example.Clops.dto.SpatialObjectRequest;
import com.example.Clops.dto.SpatialObjectResponse;
import com.example.Clops.entity.SpatialObjectType;
import com.example.Clops.service.SpatialObjectService;
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
@RequestMapping("/api/spatial-objects")
@RequiredArgsConstructor
@Validated
@Tag(name = "🗺️ Spatial Objects Management", description = "CRUD операции для управления пространственными объектами")
@SecurityRequirement(name = "bearer-key")
public class SpatialObjectController {

    private final SpatialObjectService spatialObjectService;

    @Operation(summary = "Получить все пространственные объекты", description = "Возвращает список объектов с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping
    public ResponseEntity<Page<SpatialObjectResponse>> getAllSpatialObjects(
            @Parameter(description = "Номер страницы (с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Поле для сортировки", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,

            @Parameter(description = "Направление сортировки (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SpatialObjectResponse> objects = spatialObjectService.findAll(pageable);
        return ResponseEntity.ok(objects);
    }

    @Operation(summary = "Получить объекты по типу", description = "Возвращает список объектов определенного типа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<List<SpatialObjectResponse>> getSpatialObjectsByType(
            @Parameter(description = "Тип объекта", example = "NODE", required = true)
            @PathVariable SpatialObjectType type) {

        List<SpatialObjectResponse> objects = spatialObjectService.findByType(type);
        return ResponseEntity.ok(objects);
    }

    @Operation(summary = "Получить объекты по территории", description = "Возвращает список объектов определенной территории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/territory/{territoryId}")
    public ResponseEntity<List<SpatialObjectResponse>> getSpatialObjectsByTerritory(
            @Parameter(description = "ID территории", example = "1", required = true)
            @PathVariable Integer territoryId) {

        List<SpatialObjectResponse> objects = spatialObjectService.findByTerritoryId(territoryId);
        return ResponseEntity.ok(objects);
    }

    @Operation(summary = "Поиск объектов", description = "Поиск объектов по названию или описанию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный поиск"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<SpatialObjectResponse>> searchSpatialObjects(
            @Parameter(description = "Поисковый запрос", example = "опорный", required = true)
            @RequestParam String q,

            @Parameter(description = "Номер страницы (с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<SpatialObjectResponse> objects = spatialObjectService.search(q, pageable);
        return ResponseEntity.ok(objects);
    }

    @Operation(summary = "Получить объект по ID", description = "Возвращает объект по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объект найден"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Объект не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SpatialObjectResponse> getSpatialObjectById(
            @Parameter(description = "ID объекта", example = "1", required = true)
            @PathVariable Integer id) {

        return spatialObjectService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать объект", description = "Создает новый пространственный объект")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Объект успешно создан",
                    content = @Content(schema = @Schema(implementation = SpatialObjectResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные объекта"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "409", description = "Объект с таким названием уже существует")
    })
    @PostMapping
    public ResponseEntity<?> createSpatialObject(
            @Parameter(description = "Данные объекта", required = true)
            @Valid @RequestBody SpatialObjectRequest spatialObjectRequest) {

        try {
            SpatialObjectResponse createdObject = spatialObjectService.create(spatialObjectRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdObject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Обновить объект", description = "Полностью обновляет данные объекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объект успешно обновлен"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Объект не найдена"),
            @ApiResponse(responseCode = "400", description = "Неверные данные объекта"),
            @ApiResponse(responseCode = "409", description = "Объект с таким названием уже существует")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSpatialObject(
            @Parameter(description = "ID объекта", example = "1", required = true)
            @PathVariable Integer id,

            @Parameter(description = "Новые данные объекта", required = true)
            @Valid @RequestBody SpatialObjectRequest spatialObjectRequest) {

        try {
            Optional<SpatialObjectResponse> updatedObject = spatialObjectService.update(id, spatialObjectRequest);
            return updatedObject.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Удалить объект", description = "Удаляет объект по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Объект успешно удален"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Объект не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpatialObject(
            @Parameter(description = "ID объекта", example = "1", required = true)
            @PathVariable Integer id) {

        boolean deleted = spatialObjectService.delete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Получить количество объектов на территории", description = "Возвращает количество пространственных объектов на указанной территории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение количества"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/territory/{territoryId}/count")
    public ResponseEntity<Long> getSpatialObjectsCountByTerritory(
            @Parameter(description = "ID территории", example = "1", required = true)
            @PathVariable Integer territoryId) {

        long count = spatialObjectService.countByTerritoryId(territoryId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Получить объекты по типу с пагинацией", description = "Возвращает список объектов определенного типа с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/type/{type}/page")
    public ResponseEntity<Page<SpatialObjectResponse>> getSpatialObjectsByTypeWithPagination(
            @Parameter(description = "Тип объекта", example = "NODE", required = true)
            @PathVariable SpatialObjectType type,

            @Parameter(description = "Номер страницы (с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<SpatialObjectResponse> objects = spatialObjectService.findByType(type, pageable);
        return ResponseEntity.ok(objects);
    }

    @Operation(summary = "Поиск объектов по территории", description = "Поиск объектов на определенной территории по названию или описанию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный поиск"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/territory/{territoryId}/search")
    public ResponseEntity<Page<SpatialObjectResponse>> searchSpatialObjectsByTerritory(
            @Parameter(description = "ID территории", example = "1", required = true)
            @PathVariable Integer territoryId,

            @Parameter(description = "Поисковый запрос", example = "опорный", required = true)
            @RequestParam String q,

            @Parameter(description = "Номер страницы (с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<SpatialObjectResponse> objects = spatialObjectService.searchByTerritory(territoryId, q, pageable);
        return ResponseEntity.ok(objects);
    }
}