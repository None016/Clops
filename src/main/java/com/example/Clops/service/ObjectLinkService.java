package com.example.Clops.service;

import com.example.Clops.dto.ObjectLinkRequest;
import com.example.Clops.dto.ObjectLinkResponse;
import com.example.Clops.entity.LinkType;
import com.example.Clops.entity.ObjectLink;
import com.example.Clops.repository.ObjectLinkRepository;
import com.example.Clops.repository.SpatialObjectRepository;
import com.example.Clops.service.mapper.ObjectLinkMapper;
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
public class ObjectLinkService {

    private final ObjectLinkRepository objectLinkRepository;
    private final SpatialObjectRepository spatialObjectRepository;
    private final ObjectLinkMapper objectLinkMapper;

    @Transactional(readOnly = true)
    public Page<ObjectLinkResponse> findAll(Pageable pageable) {
        return objectLinkRepository.findAll(pageable)
                .map(objectLinkMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ObjectLinkResponse> findByFromObjectId(Integer fromObjectId) {
        return objectLinkRepository.findByFromObjectId(fromObjectId).stream()
                .map(objectLinkMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ObjectLinkResponse> findByFromObjectId(Integer fromObjectId, Pageable pageable) {
        return objectLinkRepository.findByFromObjectId(fromObjectId, pageable)
                .map(objectLinkMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ObjectLinkResponse> findByToObjectId(Integer toObjectId) {
        return objectLinkRepository.findByToObjectId(toObjectId).stream()
                .map(objectLinkMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ObjectLinkResponse> findByObjectId(Integer objectId) {
        return objectLinkRepository.findByObjectId(objectId).stream()
                .map(objectLinkMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ObjectLinkResponse> findByObjectId(Integer objectId, Pageable pageable) {
        return objectLinkRepository.findByObjectId(objectId, pageable)
                .map(objectLinkMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ObjectLinkResponse> findByLinkType(LinkType linkType) {
        return objectLinkRepository.findByLinkType(linkType).stream()
                .map(objectLinkMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ObjectLinkResponse> findById(Integer id) {
        return objectLinkRepository.findById(id)
                .map(objectLinkMapper::toResponse);
    }

    @Transactional
    public ObjectLinkResponse create(ObjectLinkRequest linkRequest) {
        // Проверка существования исходного объекта
        if (!spatialObjectRepository.existsById(linkRequest.getFromObjectId())) {
            throw new IllegalArgumentException("From object not found with id: " + linkRequest.getFromObjectId());
        }

        // Проверка существования целевого объекта
        if (!spatialObjectRepository.existsById(linkRequest.getToObjectId())) {
            throw new IllegalArgumentException("To object not found with id: " + linkRequest.getToObjectId());
        }

        // Проверка, что объекты не одинаковые
        if (linkRequest.getFromObjectId().equals(linkRequest.getToObjectId())) {
            throw new IllegalArgumentException("Cannot create link to the same object");
        }

        // Проверка на дубликат связи
        Optional<ObjectLink> existingLink = objectLinkRepository.findExistingLink(
                linkRequest.getFromObjectId(),
                linkRequest.getToObjectId(),
                linkRequest.getLinkType());

        if (existingLink.isPresent()) {
            throw new IllegalArgumentException("Object link already exists");
        }

        ObjectLink link = objectLinkMapper.toEntity(linkRequest);
        ObjectLink savedLink = objectLinkRepository.save(link);
        return objectLinkMapper.toResponse(savedLink);
    }

    @Transactional
    public Optional<ObjectLinkResponse> update(Integer id, ObjectLinkRequest linkRequest) {
        Optional<ObjectLink> existingLink = objectLinkRepository.findById(id);
        if (existingLink.isEmpty()) {
            return Optional.empty();
        }

        // Проверка существования исходного объекта
        if (linkRequest.getFromObjectId() != null &&
                !spatialObjectRepository.existsById(linkRequest.getFromObjectId())) {
            throw new IllegalArgumentException("From object not found with id: " + linkRequest.getFromObjectId());
        }

        // Проверка существования целевого объекта
        if (linkRequest.getToObjectId() != null &&
                !spatialObjectRepository.existsById(linkRequest.getToObjectId())) {
            throw new IllegalArgumentException("To object not found with id: " + linkRequest.getToObjectId());
        }

        // Проверка, что объекты не одинаковые
        if (linkRequest.getFromObjectId() != null && linkRequest.getToObjectId() != null &&
                linkRequest.getFromObjectId().equals(linkRequest.getToObjectId())) {
            throw new IllegalArgumentException("Cannot create link to the same object");
        }

        ObjectLink link = existingLink.get();
        objectLinkMapper.updateEntity(linkRequest, link);
        ObjectLink updatedLink = objectLinkRepository.save(link);
        return Optional.of(objectLinkMapper.toResponse(updatedLink));
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!objectLinkRepository.existsById(id)) {
            return false;
        }

        objectLinkRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public long countByObjectId(Integer objectId) {
        return objectLinkRepository.countByObjectId(objectId);
    }

    @Transactional(readOnly = true)
    public long countByLinkType(LinkType linkType) {
        return objectLinkRepository.countByLinkType(linkType);
    }

    @Transactional(readOnly = true)
    public List<ObjectLinkResponse> findByFromAndToObjects(Integer fromObjectId, Integer toObjectId) {
        return objectLinkRepository.findByFromAndToObjects(fromObjectId, toObjectId).stream()
                .map(objectLinkMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ObjectLinkResponse> findByFromObjectAndLinkType(Integer fromObjectId, LinkType linkType) {
        return objectLinkRepository.findByFromObjectAndLinkType(fromObjectId, linkType).stream()
                .map(objectLinkMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ObjectLinkResponse> findByToObjectAndLinkType(Integer toObjectId, LinkType linkType) {
        return objectLinkRepository.findByToObjectAndLinkType(toObjectId, linkType).stream()
                .map(objectLinkMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ObjectLinkResponse> findObjectNetwork(Integer objectId) {
        return objectLinkRepository.findByObjectId(objectId).stream()
                .map(objectLinkMapper::toResponse)
                .collect(Collectors.toList());
    }
}
