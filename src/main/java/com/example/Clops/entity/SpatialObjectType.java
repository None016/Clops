package com.example.Clops.entity;

public enum SpatialObjectType {
    NODE("node"),
    CABLE("cable"),
    SPLICE_CLOSURE("splice_closure"),
    CUSTOMER("customer"),
    ETC("etc");

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
}
