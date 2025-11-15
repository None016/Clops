package com.example.Clops.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус соединения")
public enum ConnectionStatus {
    @Schema(description = "Соединено")
    CONNECTED("connected"),

    @Schema(description = "Отключено")
    DISCONNECTED("disconnected"),

    @Schema(description = "Плановые работы")
    MAINTENANCE("maintenance"),

    @Schema(description = "Авария")
    FAILURE("failure"),

    @Schema(description = "Зарезервировано")
    RESERVED("reserved");

    private final String value;

    ConnectionStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ConnectionStatus fromValue(String value) {
        for (ConnectionStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown connection status: " + value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
