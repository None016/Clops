package com.example.Clops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true, nullable = false)
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
    private String username;

    @Column(name = "password_hash", nullable = false)
    @NotBlank(message = "Password hash cannot be blank")
    @Size(min = 1, max = 255, message = "Password hash must be between 1 and 255 characters")
    private String passwordHash;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
