package com.softwareverde.logging.slf4j;

import com.softwareverde.logging.Log;
import com.softwareverde.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jLog implements Log {
    private final Logger _logger;

    public Slf4jLog(final Class<?> clazz) {
        _logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void write(final Class<?> callingClass, final LogLevel logLevel, final String nullableMessage, final Throwable nullableException) {
        switch (logLevel) {
            case TRACE: {
                _logger.trace(nullableMessage, nullableException);
            } break;

            case DEBUG: {
                _logger.debug(nullableMessage, nullableException);
            } break;

            case INFO: {
                _logger.info(nullableMessage, nullableException);
            } break;

            case WARN: {
                _logger.warn(nullableMessage, nullableException);
            } break;

            case ERROR: {
                _logger.error(nullableMessage, nullableException);
            } break;
        }
    }

    @Override
    public void flush() {
        // do nothing
    }
}
