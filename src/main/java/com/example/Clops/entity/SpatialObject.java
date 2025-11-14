package com.example.Clops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "spatial_objects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpatialObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", nullable = false)
    @NotBlank(message = "Type cannot be blank")
    @Size(max = 50, message = "Type must not exceed 50 characters")
    private String type; // node, cable, splice_closure, customer, etc.

    @Column(name = "name")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "geometry", columnDefinition = "geography")
    private String geometry;

    @Column(name = "attributes", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String attributes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "territory_id")
    @NotNull(message = "Territory is required")
    private Territory territory;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "fromObject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ObjectLink> outgoingLinks = new ArrayList<>();

    @OneToMany(mappedBy = "toObject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ObjectLink> incomingLinks = new ArrayList<>();

    @OneToMany(mappedBy = "cable", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FiberConnection> fiberConnections = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}