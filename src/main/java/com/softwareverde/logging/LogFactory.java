package com.softwareverde.logging;

public interface LogFactory {
    Log newLog(final Class<?> clazz);
}
