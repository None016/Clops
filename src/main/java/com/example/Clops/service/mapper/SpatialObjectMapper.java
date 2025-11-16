package com.example.Clops.service.mapper;

import com.example.Clops.dto.SpatialObjectRequest;
import com.example.Clops.dto.SpatialObjectResponse;
import com.example.Clops.entity.SpatialObject;
import com.example.Clops.entity.Territory;
import com.example.Clops.repository.TerritoryRepository;
import com.example.Clops.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SpatialObjectMapper {

    private final TerritoryRepository territoryRepository;
    private final GeometryUtil geometryUtil;

    public SpatialObject toEntity(SpatialObjectRequest request) {
        if (request == null) {
            return null;
        }

        SpatialObject spatialObject = new SpatialObject();
        spatialObject.setType(request.getType());
        spatialObject.setName(request.getName());
        spatialObject.setDescription(request.getDescription());

        // Конвертируем WKT в Geometry
        if (request.getGeometryWkt() != null && !request.getGeometryWkt().trim().isEmpty()) {
            spatialObject.setGeometry(geometryUtil.wktToGeometry(request.getGeometryWkt()));
        }

        spatialObject.setAttributes(request.getAttributes());

        // Установка территории, если указан territoryId
        if (request.getTerritoryId() != null) {
            Optional<Territory> territory = territoryRepository.findById(request.getTerritoryId());
            territory.ifPresent(spatialObject::setTerritory);
        }

        return spatialObject;
    }

    public SpatialObjectResponse toResponse(SpatialObject spatialObject) {
        if (spatialObject == null) {
            return null;
        }

        SpatialObjectResponse response = new SpatialObjectResponse();
        response.setId(spatialObject.getId());
        response.setType(spatialObject.getType());
        response.setName(spatialObject.getName());
        response.setDescription(spatialObject.getDescription());

        // Конвертируем Geometry в WKT
        if (spatialObject.getGeometry() != null) {
            response.setGeometryWkt(geometryUtil.geometryToWkt(spatialObject.getGeometry()));
        }

        response.setAttributes(spatialObject.getAttributes());
        response.setCreatedAt(spatialObject.getCreatedAt());

        // Установка информации о территории
        if (spatialObject.getTerritory() != null) {
            response.setTerritoryId(spatialObject.getTerritory().getId());
            response.setTerritoryName(spatialObject.getTerritory().getName());
        }

        return response;
    }

    public void updateEntity(SpatialObjectRequest request, SpatialObject spatialObject) {
        if (request == null || spatialObject == null) {
            return;
        }

        spatialObject.setType(request.getType());
        spatialObject.setName(request.getName());
        spatialObject.setDescription(request.getDescription());

        // Обновляем геометрию
        if (request.getGeometryWkt() != null && !request.getGeometryWkt().trim().isEmpty()) {
            spatialObject.setGeometry(geometryUtil.wktToGeometry(request.getGeometryWkt()));
        } else {
            spatialObject.setGeometry(null);
        }

        spatialObject.setAttributes(request.getAttributes());

        // Обновление территории, если указан territoryId
        if (request.getTerritoryId() != null) {
            Optional<Territory> territory = territoryRepository.findById(request.getTerritoryId());
            territory.ifPresent(spatialObject::setTerritory);
        } else {
            spatialObject.setTerritory(null);
        }
    }
}