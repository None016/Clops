package com.example.Clops.service.mapper;

import com.example.Clops.dto.FiberConnectionRequest;
import com.example.Clops.dto.FiberConnectionResponse;
import com.example.Clops.entity.FiberConnection;
import com.example.Clops.entity.SpatialObject;
import com.example.Clops.repository.SpatialObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FiberConnectionMapper {

    private final SpatialObjectRepository spatialObjectRepository;

    public FiberConnection toEntity(FiberConnectionRequest request) {
        if (request == null) {
            return null;
        }

        FiberConnection connection = new FiberConnection();

        // Установка кабеля
        Optional<SpatialObject> cable = spatialObjectRepository.findById(request.getCableId());
        cable.ifPresent(connection::setCable);

        // Установка целевого объекта
        Optional<SpatialObject> toObject = spatialObjectRepository.findById(request.getToObjectId());
        toObject.ifPresent(connection::setToObject);

        connection.setFromFiber(request.getFromFiber());
        connection.setToFiber(request.getToFiber());
        connection.setStatus(request.getStatus());

        return connection;
    }

    public FiberConnectionResponse toResponse(FiberConnection connection) {
        if (connection == null) {
            return null;
        }

        FiberConnectionResponse response = new FiberConnectionResponse();
        response.setId(connection.getId());
        response.setFromFiber(connection.getFromFiber());
        response.setToFiber(connection.getToFiber());
        response.setStatus(connection.getStatus());
        response.setCreatedAt(connection.getCreatedAt());

        // Информация о кабеле
        if (connection.getCable() != null) {
            response.setCableId(connection.getCable().getId());
            response.setCableName(connection.getCable().getName());
        }

        // Информация о целевом объекте
        if (connection.getToObject() != null) {
            response.setToObjectId(connection.getToObject().getId());
            response.setToObjectName(connection.getToObject().getName());
            response.setToObjectType(connection.getToObject().getType().name());
        }

        return response;
    }

    public void updateEntity(FiberConnectionRequest request, FiberConnection connection) {
        if (request == null || connection == null) {
            return;
        }

        // Обновление кабеля
        if (request.getCableId() != null) {
            Optional<SpatialObject> cable = spatialObjectRepository.findById(request.getCableId());
            cable.ifPresent(connection::setCable);
        }

        // Обновление целевого объекта
        if (request.getToObjectId() != null) {
            Optional<SpatialObject> toObject = spatialObjectRepository.findById(request.getToObjectId());
            toObject.ifPresent(connection::setToObject);
        }

        connection.setFromFiber(request.getFromFiber());
        connection.setToFiber(request.getToFiber());
        connection.setStatus(request.getStatus());
    }
}
