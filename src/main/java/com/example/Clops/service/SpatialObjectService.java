package com.example.Clops.service;

import com.example.Clops.dto.SpatialObjectRequest;
import com.example.Clops.dto.SpatialObjectResponse;
import com.example.Clops.entity.SpatialObject;
import com.example.Clops.entity.SpatialObjectType;
import com.example.Clops.repository.SpatialObjectRepository;
import com.example.Clops.repository.TerritoryRepository;
import com.example.Clops.service.mapper.SpatialObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpatialObjectService {

    private final SpatialObjectRepository spatialObjectRepository;
    private final TerritoryRepository territoryRepository;
    private final SpatialObjectMapper spatialObjectMapper;

    @Transactional(readOnly = true)
    public Page<SpatialObjectResponse> findAll(Pageable pageable) {
        return spatialObjectRepository.findAll(pageable)
                .map(spatialObjectMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<SpatialObjectResponse> findByType(SpatialObjectType type, Pageable pageable) {
        return spatialObjectRepository.findByType(type, pageable)
                .map(spatialObjectMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<SpatialObjectResponse> findByTerritoryId(Integer territoryId, Pageable pageable) {
        return spatialObjectRepository.findByTerritoryId(territoryId, pageable)
                .map(spatialObjectMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<SpatialObjectResponse> search(String search, Pageable pageable) {
        return spatialObjectRepository.searchByNameOrDescription(search, pageable)
                .map(spatialObjectMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<SpatialObjectResponse> searchByTerritory(Integer territoryId, String search, Pageable pageable) {
        return spatialObjectRepository.searchByTerritoryAndNameOrDescription(territoryId, search, pageable)
                .map(spatialObjectMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<SpatialObjectResponse> findById(Integer id) {
        return spatialObjectRepository.findById(id)
                .map(spatialObjectMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<SpatialObjectResponse> findByName(String name) {
        return spatialObjectRepository.findByName(name)
                .map(spatialObjectMapper::toResponse);
    }

    @Transactional
    public SpatialObjectResponse create(SpatialObjectRequest spatialObjectRequest) {
        if (spatialObjectRepository.existsByName(spatialObjectRequest.getName())) {
            throw new IllegalArgumentException("Spatial object with this name already exists: " + spatialObjectRequest.getName());
        }

        // Проверка существования территории, если указана
        if (spatialObjectRequest.getTerritoryId() != null &&
                !territoryRepository.existsById(spatialObjectRequest.getTerritoryId())) {
            throw new IllegalArgumentException("Territory not found with id: " + spatialObjectRequest.getTerritoryId());
        }

        SpatialObject spatialObject = spatialObjectMapper.toEntity(spatialObjectRequest);
        SpatialObject savedSpatialObject = spatialObjectRepository.save(spatialObject);
        return spatialObjectMapper.toResponse(savedSpatialObject);
    }

    @Transactional
    public Optional<SpatialObjectResponse> update(Integer id, SpatialObjectRequest spatialObjectRequest) {
        Optional<SpatialObject> existingSpatialObject = spatialObjectRepository.findById(id);
        if (existingSpatialObject.isEmpty()) {
            return Optional.empty();
        }

        SpatialObject spatialObject = existingSpatialObject.get();

        // Проверка уникальности name (исключая текущий объект)
        if (!spatialObject.getName().equals(spatialObjectRequest.getName()) &&
                spatialObjectRepository.existsByNameAndIdNot(spatialObjectRequest.getName(), id)) {
            throw new IllegalArgumentException("Spatial object with this name already exists: " + spatialObjectRequest.getName());
        }

        // Проверка существования территории, если указана
        if (spatialObjectRequest.getTerritoryId() != null &&
                !territoryRepository.existsById(spatialObjectRequest.getTerritoryId())) {
            throw new IllegalArgumentException("Territory not found with id: " + spatialObjectRequest.getTerritoryId());
        }

        spatialObjectMapper.updateEntity(spatialObjectRequest, spatialObject);
        SpatialObject updatedSpatialObject = spatialObjectRepository.save(spatialObject);
        return Optional.of(spatialObjectMapper.toResponse(updatedSpatialObject));
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!spatialObjectRepository.existsById(id)) {
            return false;
        }

        spatialObjectRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public long countByTerritoryId(Integer territoryId) {
        return spatialObjectRepository.countByTerritoryId(territoryId);
    }

    @Transactional(readOnly = true)
    public List<SpatialObjectResponse> findByType(SpatialObjectType type) {
        return spatialObjectRepository.findByType(type).stream()
                .map(spatialObjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SpatialObjectResponse> findByTerritoryId(Integer territoryId) {
        return spatialObjectRepository.findByTerritoryId(territoryId).stream()
                .map(spatialObjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Метод для поиска объектов в радиусе (требует PostGIS)
    @Transactional(readOnly = true)
    public List<SpatialObjectResponse> findWithinDistance(String pointWkt, double distanceMeters) {
        return spatialObjectRepository.findWithinDistance(pointWkt, distanceMeters).stream()
                .map(spatialObjectMapper::toResponse)
                .collect(Collectors.toList());
    }
}
