package com.softwareverde.logging;

public interface Log {
    void write(Class<?> callingClass, LogLevel logLevel, String nullableMessage, Throwable nullableException);
    default void flush() { }
}
