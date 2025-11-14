package com.example.Clops.service.mapper;

import com.example.Clops.dto.UserRequest;
import com.example.Clops.dto.UserResponse;
import com.example.Clops.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequest userRequest);

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    void updateEntity(UserRequest userRequest, @org.mapstruct.MappingTarget User user);
}