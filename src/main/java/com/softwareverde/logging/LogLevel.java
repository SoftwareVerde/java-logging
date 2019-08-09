package com.softwareverde.logging;

public enum LogLevel {
    /**
     * Logging is ignored.
     */
    OFF(0),

    /**
     * TRACE is used to follow the minutia of a procedure.
     *  The most detailed level of logging.
     *  TRACE statements are very rarely logged in production environments.
     */
    TRACE(1),

    /**
     * DEBUG may be used to inform the developer of how the operation of a component is proceeding.
     *  DEBUG statements are typically not logged in production environments unless an issue is being actively investigated.
     */
    DEBUG(2),

    /**
     * INFO statements are non-problematic and are neutral in nature.
     *  INFO should be used sparingly in library code and should provide only important insight to the developer.
     *  INFO statements may be logged in production environments.
     */
    INFO(3),

    /**
     * WARN statements indicate that an important issue might have occurred.
     *  A WARN statement may not be a problem, but it represents a statement that may be a problem in certain contexts.
     *  WARN statements are most likely logged in production environments.
     *  WARN statements may indicate that a recovery occurred.
     */
    WARN(4),

    /**
     * ERROR statements indicate a failure in operation.
     *  An ERROR is almost certainly a problem, including a failure to abide by a component's contract.
     *  Whenever possible, an ERROR should have an Exception associated with it.
     *  ERROR statements should always be logged in production environments.
     *  ERROR statements may indicate that a recovery was not possible.
     */
    ERROR(5);

    public static final LogLevel ON = LogLevel.TRACE;

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
