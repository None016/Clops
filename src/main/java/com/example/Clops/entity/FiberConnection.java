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
@Table(name = "fiber_connections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiberConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cable_id")
    @NotNull(message = "Cable is required")
    private SpatialObject cable;

    @Column(name = "from_fiber")
    @NotNull(message = "From fiber is required")
    private Integer fromFiber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_object_id")
    @NotNull(message = "To object is required")
    private SpatialObject toObject;

    @Column(name = "to_fiber")
    @NotNull(message = "To fiber is required")
    private Integer toFiber;

    @Column(name = "status", nullable = false)
    @NotBlank(message = "Status cannot be blank")
    @Size(max = 50, message = "Status must not exceed 50 characters")
    private String status; // connected, broken

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}