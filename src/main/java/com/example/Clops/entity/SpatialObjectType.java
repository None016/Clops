package com.example.Clops.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип пространственного объекта")
public enum SpatialObjectType {
    @Schema(description = "Узел связи")
    node("node"),

    @Schema(description = "Кабель")
    cable("cable"),

    @Schema(description = "Муфта")
    splice_closure("splice_closure"),

    @Schema(description = "Клиент")
    customer("customer"),

    @Schema(description = "Другой объект")
    etc("etc");

    private final String value;

    SpatialObjectType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SpatialObjectType fromValue(String value) {
        for (SpatialObjectType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown spatial object type: " + value);
    }

    // Для Jackson сериализации/десериализации
    @Override
    public String toString() {
        return this.value;
    }
}
