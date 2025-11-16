package com.example.Clops.service;

import com.example.Clops.dto.FiberConnectionRequest;
import com.example.Clops.dto.FiberConnectionResponse;
import com.example.Clops.entity.ConnectionStatus;
import com.example.Clops.entity.FiberConnection;
import com.example.Clops.entity.SpatialObjectType; // Добавьте этот импорт
import com.example.Clops.repository.FiberConnectionRepository;
import com.example.Clops.repository.SpatialObjectRepository;
import com.example.Clops.service.mapper.FiberConnectionMapper;
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
public class FiberConnectionService {

    private final FiberConnectionRepository fiberConnectionRepository;
    private final SpatialObjectRepository spatialObjectRepository;
    private final FiberConnectionMapper fiberConnectionMapper;

    @Transactional(readOnly = true)
    public Page<FiberConnectionResponse> findAll(Pageable pageable) {
        return fiberConnectionRepository.findAll(pageable)
                .map(fiberConnectionMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<FiberConnectionResponse> findByCableId(Integer cableId) {
        return fiberConnectionRepository.findByCableId(cableId).stream()
                .map(fiberConnectionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<FiberConnectionResponse> findByCableId(Integer cableId, Pageable pageable) {
        return fiberConnectionRepository.findByCableId(cableId, pageable)
                .map(fiberConnectionMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<FiberConnectionResponse> findByToObjectId(Integer toObjectId) {
        return fiberConnectionRepository.findByToObjectId(toObjectId).stream()
                .map(fiberConnectionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FiberConnectionResponse> findByStatus(ConnectionStatus status) {
        return fiberConnectionRepository.findByStatus(status).stream()
                .map(fiberConnectionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<FiberConnectionResponse> findById(Integer id) {
        return fiberConnectionRepository.findById(id)
                .map(fiberConnectionMapper::toResponse);
    }

    @Transactional
    public FiberConnectionResponse create(FiberConnectionRequest connectionRequest) {
        // Проверка существования кабеля
        if (!spatialObjectRepository.existsById(connectionRequest.getCableId())) {
            throw new IllegalArgumentException("Cable not found with id: " + connectionRequest.getCableId());
        }

        // Проверка, что кабель действительно является кабелем
        var cable = spatialObjectRepository.findById(connectionRequest.getCableId());
        if (cable.isPresent() && cable.get().getType() != SpatialObjectType.cable) {
            throw new IllegalArgumentException("Spatial object with id " + connectionRequest.getCableId() + " is not a cable");
        }

        // Проверка существования целевого объекта
        if (!spatialObjectRepository.existsById(connectionRequest.getToObjectId())) {
            throw new IllegalArgumentException("Target object not found with id: " + connectionRequest.getToObjectId());
        }

        // Проверка на дубликат соединения
        Optional<FiberConnection> existingConnection = fiberConnectionRepository.findExistingConnection(
                connectionRequest.getCableId(),
                connectionRequest.getFromFiber(),
                connectionRequest.getToObjectId(),
                connectionRequest.getToFiber());

        if (existingConnection.isPresent()) {
            throw new IllegalArgumentException("Fiber connection already exists");
        }

        FiberConnection connection = fiberConnectionMapper.toEntity(connectionRequest);
        FiberConnection savedConnection = fiberConnectionRepository.save(connection);
        return fiberConnectionMapper.toResponse(savedConnection);
    }

    @Transactional
    public Optional<FiberConnectionResponse> update(Integer id, FiberConnectionRequest connectionRequest) {
        Optional<FiberConnection> existingConnection = fiberConnectionRepository.findById(id);
        if (existingConnection.isEmpty()) {
            return Optional.empty();
        }

        // Проверка существования кабеля
        if (connectionRequest.getCableId() != null &&
                !spatialObjectRepository.existsById(connectionRequest.getCableId())) {
            throw new IllegalArgumentException("Cable not found with id: " + connectionRequest.getCableId());
        }

        // Проверка существования целевого объекта
        if (connectionRequest.getToObjectId() != null &&
                !spatialObjectRepository.existsById(connectionRequest.getToObjectId())) {
            throw new IllegalArgumentException("Target object not found with id: " + connectionRequest.getToObjectId());
        }

        FiberConnection connection = existingConnection.get();
        fiberConnectionMapper.updateEntity(connectionRequest, connection);
        FiberConnection updatedConnection = fiberConnectionRepository.save(connection);
        return Optional.of(fiberConnectionMapper.toResponse(updatedConnection));
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!fiberConnectionRepository.existsById(id)) {
            return false;
        }

        fiberConnectionRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Optional<FiberConnectionResponse> updateStatus(Integer id, ConnectionStatus status) {
        Optional<FiberConnection> connection = fiberConnectionRepository.findById(id);
        if (connection.isPresent()) {
            connection.get().setStatus(status);
            FiberConnection updatedConnection = fiberConnectionRepository.save(connection.get());
            return Optional.of(fiberConnectionMapper.toResponse(updatedConnection));
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public long countByCableId(Integer cableId) {
        return fiberConnectionRepository.countByCableId(cableId);
    }

    @Transactional(readOnly = true)
    public long countByObjectId(Integer objectId) {
        return fiberConnectionRepository.countByObjectId(objectId);
    }

    @Transactional(readOnly = true)
    public List<FiberConnectionResponse> findByCableAndFiber(Integer cableId, Integer fiber) {
        return fiberConnectionRepository.findByCableAndFiber(cableId, fiber).stream()
                .map(fiberConnectionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FiberConnectionResponse> findByCableAndStatus(Integer cableId, ConnectionStatus status) {
        return fiberConnectionRepository.findByCableAndStatus(cableId, status).stream()
                .map(fiberConnectionMapper::toResponse)
                .collect(Collectors.toList());
    }
}