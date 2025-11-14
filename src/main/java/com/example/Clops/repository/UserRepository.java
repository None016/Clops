package com.example.Clops.repository;


import com.example.Clops.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Integer id);

    Page<User> findAll(Pageable pageable);

    Page<User> findByIsActive(Boolean isActive, Pageable pageable);
}