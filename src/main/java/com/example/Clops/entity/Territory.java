package com.example.Clops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

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
    @Size(min = 1, max = 255, message = "Territory name must be between 1 and 255 characters")
    private String name;

    @Column(name = "description")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @OneToMany(mappedBy = "territory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SpatialObject> spatialObjects = new ArrayList<>();
}