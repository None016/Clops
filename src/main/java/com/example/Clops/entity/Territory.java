package com.example.Clops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "territories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Territory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Territory name cannot be blank")
    @Size(min = 2, max = 255, message = "Territory name must be between 2 and 255 characters")
    private String name;

    @Column(name = "description")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}