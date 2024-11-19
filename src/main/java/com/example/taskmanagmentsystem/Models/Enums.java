package com.example.taskmanagmentsystem.Models;

import java.util.Arrays;

public class Enums {

    public enum Status {
        SENT, PENDING, COMPLETE
    }

    public enum Priority {
        LOW, NORMAL, HIGH
    }

    public <T extends Enum<T>> T fromString(String role, Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Такой роли нет: " + role));
    }
}
