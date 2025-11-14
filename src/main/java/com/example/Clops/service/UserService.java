package com.example.Clops.service;


import com.example.Clops.dto.UserRequest;
import com.example.Clops.dto.UserResponse;
import com.example.Clops.entity.User;
import com.example.Clops.repository.UserRepository;
import com.example.Clops.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findByActiveStatus(Boolean isActive, Pageable pageable) {
        return userRepository.findByIsActive(isActive, pageable)
                .map(userMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toResponse);
    }

    @Transactional
    public UserResponse create(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userRequest.getUsername());
        }

        User user = userMapper.toEntity(userRequest);
        // Хэшируем пароль перед сохранением
        user.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
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

        user.setUsername(userRequest.getUsername());

        // Если пароль изменен, кодируем новый пароль
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        }

        user.setIsActive(userRequest.getIsActive());
        User updatedUser = userRepository.save(user);
        return Optional.of(userMapper.toResponse(updatedUser));
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
            return Optional.of(userMapper.toResponse(updatedUser));
        }
        return Optional.empty();
    }

    /**
     * Смена пароля (для зарегистрированных пользователей)
     * @param id ID пользователя
     * @param oldPassword старый пароль
     * @param newPassword новый пароль
     * @return true если пароль успешно изменен
     */
    @Transactional
    public boolean changePassword(Integer id, String oldPassword, String newPassword) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && passwordEncoder.matches(oldPassword, user.get().getPasswordHash())) {
            user.get().setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
}