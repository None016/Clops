package com.example.Clops.service;


import com.example.Clops.dto.TerritoryRequest;
import com.example.Clops.dto.TerritoryResponse;
import com.example.Clops.entity.Territory;
import com.example.Clops.repository.TerritoryRepository;
import com.example.Clops.service.mapper.TerritoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TerritoryService {

    private final TerritoryRepository territoryRepository;
    private final TerritoryMapper territoryMapper;

    @Transactional(readOnly = true)
    public Page<TerritoryResponse> findAll(Pageable pageable) {
        return territoryRepository.findAll(pageable)
                .map(territoryMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<TerritoryResponse> search(String search, Pageable pageable) {
        return territoryRepository.searchByNameOrDescription(search, pageable)
                .map(territoryMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<TerritoryResponse> findById(Integer id) {
        return territoryRepository.findById(id)
                .map(territoryMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<TerritoryResponse> findByName(String name) {
        return territoryRepository.findByName(name)
                .map(territoryMapper::toResponse);
    }

    @Transactional
    public TerritoryResponse create(TerritoryRequest territoryRequest) {
        if (territoryRepository.existsByName(territoryRequest.getName())) {
            throw new IllegalArgumentException("Territory with this name already exists: " + territoryRequest.getName());
        }

        Territory territory = territoryMapper.toEntity(territoryRequest);
        Territory savedTerritory = territoryRepository.save(territory);
        return territoryMapper.toResponse(savedTerritory);
    }

    @Transactional
    public Optional<TerritoryResponse> update(Integer id, TerritoryRequest territoryRequest) {
        Optional<Territory> existingTerritory = territoryRepository.findById(id);
        if (existingTerritory.isEmpty()) {
            return Optional.empty();
        }

        Territory territory = existingTerritory.get();

        // Проверка уникальности name (исключая текущую территорию)
        if (!territory.getName().equals(territoryRequest.getName()) &&
                territoryRepository.existsByNameAndIdNot(territoryRequest.getName(), id)) {
            throw new IllegalArgumentException("Territory with this name already exists: " + territoryRequest.getName());
        }

        territoryMapper.updateEntity(territoryRequest, territory);
        Territory updatedTerritory = territoryRepository.save(territory);
        return Optional.of(territoryMapper.toResponse(updatedTerritory));
    }

//    @Transactional
//    public boolean delete(Integer id) {
//        if (!territoryRepository.existsById(id)) {
//            return false;
//        }
//
//        // Проверка на наличие связанных spatial objects
//        if (territoryRepository.hasSpatialObjects(id)) {
//            throw new IllegalStateException("Cannot delete territory with associated spatial objects");
//        }
//
//        territoryRepository.deleteById(id);
//        return true;
//    }

    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return territoryRepository.existsById(id);
    }
}
