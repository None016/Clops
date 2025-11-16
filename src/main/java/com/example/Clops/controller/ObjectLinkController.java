package com.example.Clops.controller;

import com.example.Clops.dto.ObjectLinkRequest;
import com.example.Clops.dto.ObjectLinkResponse;
import com.example.Clops.entity.LinkType;
import com.example.Clops.service.ObjectLinkService;
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
@RequestMapping("/api/object-links")
@RequiredArgsConstructor
@Validated
@Tag(name = "🔗 Object Links Management", description = "CRUD операции для управления связями между объектами")
@SecurityRequirement(name = "bearer-key")
public class ObjectLinkController {

    private final ObjectLinkService objectLinkService;

    @Operation(summary = "Получить все связи", description = "Возвращает список связей между объектами с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping
    public ResponseEntity<Page<ObjectLinkResponse>> getAllObjectLinks(
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
        Page<ObjectLinkResponse> links = objectLinkService.findAll(pageable);
        return ResponseEntity.ok(links);
    }

    @Operation(summary = "Получить связи по исходному объекту", description = "Возвращает список связей для указанного исходного объекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/from/{fromObjectId}")
    public ResponseEntity<List<ObjectLinkResponse>> getLinksByFromObject(
            @Parameter(description = "ID исходного объекта", example = "1", required = true)
            @PathVariable Integer fromObjectId) {

        List<ObjectLinkResponse> links = objectLinkService.findByFromObjectId(fromObjectId);
        return ResponseEntity.ok(links);
    }

    @Operation(summary = "Получить связи по целевому объекту", description = "Возвращает список связей для указанного целевого объекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/to/{toObjectId}")
    public ResponseEntity<List<ObjectLinkResponse>> getLinksByToObject(
            @Parameter(description = "ID целевого объекта", example = "2", required = true)
            @PathVariable Integer toObjectId) {

        List<ObjectLinkResponse> links = objectLinkService.findByToObjectId(toObjectId);
        return ResponseEntity.ok(links);
    }

    @Operation(summary = "Получить все связи объекта", description = "Возвращает все связи указанного объекта (входящие и исходящие)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/object/{objectId}")
    public ResponseEntity<List<ObjectLinkResponse>> getLinksByObject(
            @Parameter(description = "ID объекта", example = "1", required = true)
            @PathVariable Integer objectId) {

        List<ObjectLinkResponse> links = objectLinkService.findByObjectId(objectId);
        return ResponseEntity.ok(links);
    }

    @Operation(summary = "Получить связи по типу", description = "Возвращает список связей с указанным типом")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/type/{linkType}")
    public ResponseEntity<List<ObjectLinkResponse>> getLinksByType(
            @Parameter(description = "Тип связи", example = "OPTICAL", required = true)
            @PathVariable LinkType linkType) {

        List<ObjectLinkResponse> links = objectLinkService.findByLinkType(linkType);
        return ResponseEntity.ok(links);
    }

    @Operation(summary = "Получить связь по ID", description = "Возвращает связь по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Связь найдена"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Связь не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ObjectLinkResponse> getObjectLinkById(
            @Parameter(description = "ID связи", example = "1", required = true)
            @PathVariable Integer id) {

        return objectLinkService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать связь", description = "Создает новую связь между объектами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Связь успешно создана",
                    content = @Content(schema = @Schema(implementation = ObjectLinkResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные связи"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "409", description = "Связь уже существует")
    })
    @PostMapping
    public ResponseEntity<?> createObjectLink(
            @Parameter(description = "Данные связи", required = true)
            @Valid @RequestBody ObjectLinkRequest linkRequest) {

        try {
            ObjectLinkResponse createdLink = objectLinkService.create(linkRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLink);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Обновить связь", description = "Полностью обновляет данные связи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Связь успешно обновлена"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Связь не найдена"),
            @ApiResponse(responseCode = "400", description = "Неверные данные связи")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateObjectLink(
            @Parameter(description = "ID связи", example = "1", required = true)
            @PathVariable Integer id,

            @Parameter(description = "Новые данные связи", required = true)
            @Valid @RequestBody ObjectLinkRequest linkRequest) {

        try {
            Optional<ObjectLinkResponse> updatedLink = objectLinkService.update(id, linkRequest);
            return updatedLink.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Удалить связь", description = "Удаляет связь по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Связь успешно удалена"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Связь не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObjectLink(
            @Parameter(description = "ID связи", example = "1", required = true)
            @PathVariable Integer id) {

        boolean deleted = objectLinkService.delete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Получить количество связей объекта", description = "Возвращает количество связей для указанного объекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение количества"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/object/{objectId}/count")
    public ResponseEntity<Long> getLinksCountByObject(
            @Parameter(description = "ID объекта", example = "1", required = true)
            @PathVariable Integer objectId) {

        long count = objectLinkService.countByObjectId(objectId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Получить связи между объектами", description = "Возвращает связи между указанными объектами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/from/{fromObjectId}/to/{toObjectId}")
    public ResponseEntity<List<ObjectLinkResponse>> getLinksBetweenObjects(
            @Parameter(description = "ID исходного объекта", example = "1", required = true)
            @PathVariable Integer fromObjectId,

            @Parameter(description = "ID целевого объекта", example = "2", required = true)
            @PathVariable Integer toObjectId) {

        List<ObjectLinkResponse> links = objectLinkService.findByFromAndToObjects(fromObjectId, toObjectId);
        return ResponseEntity.ok(links);
    }

    @Operation(summary = "Получить сеть объекта", description = "Возвращает все связи объекта (сеть подключений)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение сети"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/object/{objectId}/network")
    public ResponseEntity<List<ObjectLinkResponse>> getObjectNetwork(
            @Parameter(description = "ID объекта", example = "1", required = true)
            @PathVariable Integer objectId) {

        List<ObjectLinkResponse> network = objectLinkService.findObjectNetwork(objectId);
        return ResponseEntity.ok(network);
    }
}