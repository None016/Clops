package com.example.Clops.service.mapper;


import com.example.Clops.dto.UserRequest;
import com.example.Clops.dto.UserResponse;
import com.example.Clops.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toEntity(UserRequest userRequest) {
        if (userRequest == null) {
            return null;
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        // Пароль НЕ хэшируем здесь - это будет сделано в сервисе
        user.setPasswordHash(userRequest.getPassword()); // Временное значение
        user.setIsActive(userRequest.getIsActive());
        return user;
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setIsActive(user.getIsActive());
        return response;
    }

    public void updateEntity(UserRequest userRequest, User user) {
        if (userRequest == null || user == null) {
            return;
        }

        user.setUsername(userRequest.getUsername());
        // Пароль обновляется в сервисе
        user.setIsActive(userRequest.getIsActive());
    }
}