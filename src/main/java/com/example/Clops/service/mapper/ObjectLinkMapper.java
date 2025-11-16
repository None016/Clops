package com.example.Clops.service.mapper;

import com.example.Clops.dto.ObjectLinkRequest;
import com.example.Clops.dto.ObjectLinkResponse;
import com.example.Clops.entity.ObjectLink;
import com.example.Clops.entity.SpatialObject;
import com.example.Clops.repository.SpatialObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ObjectLinkMapper {

    private final SpatialObjectRepository spatialObjectRepository;

    public ObjectLink toEntity(ObjectLinkRequest request) {
        if (request == null) {
            return null;
        }

        ObjectLink link = new ObjectLink();

        // Установка исходного объекта
        Optional<SpatialObject> fromObject = spatialObjectRepository.findById(request.getFromObjectId());
        fromObject.ifPresent(link::setFromObject);

        // Установка целевого объекта
        Optional<SpatialObject> toObject = spatialObjectRepository.findById(request.getToObjectId());
        toObject.ifPresent(link::setToObject);

        link.setLinkType(request.getLinkType());

        return link;
    }

    public ObjectLinkResponse toResponse(ObjectLink link) {
        if (link == null) {
            return null;
        }

        ObjectLinkResponse response = new ObjectLinkResponse();
        response.setId(link.getId());
        response.setLinkType(link.getLinkType());
        response.setCreatedAt(link.getCreatedAt());

        // Информация об исходном объекте
        if (link.getFromObject() != null) {
            response.setFromObjectId(link.getFromObject().getId());
            response.setFromObjectName(link.getFromObject().getName());
            response.setFromObjectType(link.getFromObject().getType().name());
        }

        // Информация о целевом объекте
        if (link.getToObject() != null) {
            response.setToObjectId(link.getToObject().getId());
            response.setToObjectName(link.getToObject().getName());
            response.setToObjectType(link.getToObject().getType().name());
        }

        return response;
    }

    public void updateEntity(ObjectLinkRequest request, ObjectLink link) {
        if (request == null || link == null) {
            return;
        }

        // Обновление исходного объекта
        if (request.getFromObjectId() != null) {
            Optional<SpatialObject> fromObject = spatialObjectRepository.findById(request.getFromObjectId());
            fromObject.ifPresent(link::setFromObject);
        }

        // Обновление целевого объекта
        if (request.getToObjectId() != null) {
            Optional<SpatialObject> toObject = spatialObjectRepository.findById(request.getToObjectId());
            toObject.ifPresent(link::setToObject);
        }

        link.setLinkType(request.getLinkType());
    }
}
