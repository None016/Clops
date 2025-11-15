package com.example.Clops.controller;

import com.example.Clops.dto.TerritoryRequest;
import com.example.Clops.dto.TerritoryResponse;
import com.example.Clops.service.TerritoryService;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/territories")
@RequiredArgsConstructor
@Validated
@Tag(name = "🗺️ Territories Management", description = "CRUD операции для управления территориями")
@SecurityRequirement(name = "bearer-key")
public class TerritoryController {

    private final TerritoryService territoryService;

    @Operation(summary = "Получить все территории", description = "Возвращает список территорий с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping
    public ResponseEntity<Page<TerritoryResponse>> getAllTerritories(
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
        Page<TerritoryResponse> territories = territoryService.findAll(pageable);
        return ResponseEntity.ok(territories);
    }

    @Operation(summary = "Поиск территорий", description = "Поиск территорий по названию или описанию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный поиск"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<TerritoryResponse>> searchTerritories(
            @Parameter(description = "Поисковый запрос", example = "центр", required = true)
            @RequestParam String q,

            @Parameter(description = "Номер страницы (с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<TerritoryResponse> territories = territoryService.search(q, pageable);
        return ResponseEntity.ok(territories);
    }

    @Operation(summary = "Получить территорию по ID", description = "Возвращает территорию по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Территория найдена"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Территория не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TerritoryResponse> getTerritoryById(
            @Parameter(description = "ID территории", example = "1", required = true)
            @PathVariable Integer id) {

        return territoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить территорию по названию", description = "Возвращает территорию по названию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Территория найдена"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Территория не найдена")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<TerritoryResponse> getTerritoryByName(
            @Parameter(description = "Название территории", example = "Центральный район", required = true)
            @PathVariable String name) {

        return territoryService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать территорию", description = "Создает новую территорию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Территория успешно создана",
                    content = @Content(schema = @Schema(implementation = TerritoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные территории"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "409", description = "Территория с таким названием уже существует")
    })
    @PostMapping
    public ResponseEntity<?> createTerritory(
            @Parameter(description = "Данные территории", required = true)
            @Valid @RequestBody TerritoryRequest territoryRequest) {

        try {
            TerritoryResponse createdTerritory = territoryService.create(territoryRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTerritory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Обновить территорию", description = "Полностью обновляет данные территории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Территория успешно обновлена"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Территория не найдена"),
            @ApiResponse(responseCode = "400", description = "Неверные данные территории"),
            @ApiResponse(responseCode = "409", description = "Территория с таким названием уже существует")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTerritory(
            @Parameter(description = "ID территории", example = "1", required = true)
            @PathVariable Integer id,

            @Parameter(description = "Новые данные территории", required = true)
            @Valid @RequestBody TerritoryRequest territoryRequest) {

        try {
            Optional<TerritoryResponse> updatedTerritory = territoryService.update(id, territoryRequest);
            return updatedTerritory.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @Operation(summary = "Удалить территорию", description = "Удаляет территорию по ID")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "Территория успешно удалена"),
//            @ApiResponse(responseCode = "400", description = "Невозможно удалить территорию с привязанными объектами"),
//            @ApiResponse(responseCode = "401", description = "Не авторизован"),
//            @ApiResponse(responseCode = "404", description = "Территория не найдена")
//    })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteTerritory(
//            @Parameter(description = "ID территории", example = "1", required = true)
//            @PathVariable Integer id) {
//
//        try {
//            boolean deleted = territoryService.delete(id);
//            return deleted ? ResponseEntity.noContent().build()
//                    : ResponseEntity.notFound().build();
//        } catch (IllegalStateException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}
