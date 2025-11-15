package com.example.Clops.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "fiber_connections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FiberConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cable_id", foreignKey = @ForeignKey(name = "fiber_connections_cable_id_fkey"))
    @NotNull(message = "Cable cannot be null")
    private SpatialObject cable;

    @Column(name = "from_fiber", nullable = false)
    @NotNull(message = "From fiber cannot be null")
    private Integer fromFiber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_object_id", foreignKey = @ForeignKey(name = "fiber_connections_to_object_id_fkey"))
    @NotNull(message = "To object cannot be null")
    private SpatialObject toObject;

    @Column(name = "to_fiber", nullable = false)
    @NotNull(message = "To fiber cannot be null")
    private Integer toFiber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ConnectionStatus status = ConnectionStatus.CONNECTED;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
