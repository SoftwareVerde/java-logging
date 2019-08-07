package com.softwareverde.logging;

public interface Log {
    void write(LogLevel logLevel, String nullableMessage, Throwable nullableException);
}
