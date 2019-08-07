package com.softwareverde.logging;

public enum LogLevel {
    OFF(0), DEBUG(1), INFO(2), WARN(3), ERROR(4);

    public static LogLevel fromValue(final Integer value) {
        for (final LogLevel logLevel : LogLevel.values()) {
            if (logLevel.value == value) {
                return logLevel;
            }
        }

        return null;
    }

    public static LogLevel fromString(final String value) {
        for (final LogLevel logLevel : LogLevel.values()) {
            if (logLevel.name().equalsIgnoreCase(value)) {
                return logLevel;
            }
        }

        return null;
    }

    public final int value;

    LogLevel(final int value) {
        this.value = value;
    }
}
