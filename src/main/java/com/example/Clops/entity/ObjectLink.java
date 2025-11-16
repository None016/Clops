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
@Table(name = "object_links")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ObjectLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_object_id", foreignKey = @ForeignKey(name = "object_links_from_object_id_fkey"))
    @NotNull(message = "From object cannot be null")
    private SpatialObject fromObject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_object_id", foreignKey = @ForeignKey(name = "object_links_to_object_id_fkey"))
    @NotNull(message = "To object cannot be null")
    private SpatialObject toObject;

    @Enumerated(EnumType.STRING)
    @Column(name = "link_type", nullable = false, length = 20)
    @NotNull(message = "Link type cannot be null")
    private LinkType linkType;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
