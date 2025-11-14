package com.example.Clops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "object_links")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_object_id")
    @NotNull(message = "From object is required")
    private SpatialObject fromObject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_object_id")
    @NotNull(message = "To object is required")
    private SpatialObject toObject;

    @Column(name = "link_type", nullable = false)
    @NotBlank(message = "Link type cannot be blank")
    @Size(max = 50, message = "Link type must not exceed 50 characters")
    private String linkType; // physical, logical

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}