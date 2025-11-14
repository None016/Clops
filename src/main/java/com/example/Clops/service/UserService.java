package com.example.Clops.service;

import com.example.Clops.dto.UserRequest;
import com.example.Clops.dto.UserResponse;
import com.example.Clops.entity.User;
import com.example.Clops.service.mapper.UserMapper;
import com.example.Clops.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper.INSTANCE::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findByActiveStatus(Boolean isActive, Pageable pageable) {
        return userRepository.findByIsActive(isActive, pageable)
                .map(UserMapper.INSTANCE::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> findById(Integer id) {
        return userRepository.findById(id)
                .map(UserMapper.INSTANCE::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper.INSTANCE::toResponse);
    }

    @Transactional
    public UserResponse create(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userRequest.getUsername());
        }

        User user = UserMapper.INSTANCE.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.toResponse(savedUser);
    }

    @Transactional
    public Optional<UserResponse> update(Integer id, UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return Optional.empty();
        }

        User user = existingUser.get();

        // Проверка уникальности username (исключая текущего пользователя)
        if (!user.getUsername().equals(userRequest.getUsername()) &&
                userRepository.existsByUsernameAndIdNot(userRequest.getUsername(), id)) {
            throw new IllegalArgumentException("Username already exists: " + userRequest.getUsername());
        }

        UserMapper.INSTANCE.updateEntity(userRequest, user);
        User updatedUser = userRepository.save(user);
        return Optional.of(UserMapper.INSTANCE.toResponse(updatedUser));
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Optional<UserResponse> deactivate(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setIsActive(false);
            User updatedUser = userRepository.save(user.get());
            return Optional.of(UserMapper.INSTANCE.toResponse(updatedUser));
        }
        return Optional.empty();
    }
}
