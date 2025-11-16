package com.example.Clops.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип связи между объектами")
public enum LinkType {
    @Schema(description = "Оптическое соединение")
    OPTICAL("optical"),

    @Schema(description = "Электрическое соединение")
    ELECTRICAL("electrical"),

    @Schema(description = "Логическая связь")
    LOGICAL("logical"),

    @Schema(description = "Физическая связь")
    PHYSICAL("physical"),

    @Schema(description = "Административная связь")
    ADMINISTRATIVE("administrative"),

    @Schema(description = "Резервная связь")
    BACKUP("backup"),

    @Schema(description = "Основная связь")
    PRIMARY("primary");

    private final String value;

    LinkType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LinkType fromValue(String value) {
        for (LinkType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown link type: " + value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}