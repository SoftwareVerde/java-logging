package com.softwareverde.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Provides static functions for logging.</p>
 *
 * <p>Methods that accept a class object as their first parameter will create a logger for that class.</p>
 */
public class Log {
    private static final Logger _logger = LoggerFactory.getLogger("com.softwareverde");

    public static void debug(final String message) {
        _logger.debug(message);
    }

    public static void debug(final Class<?> callingClass, final String message) {
        final Logger logger = LoggerFactory.getLogger(callingClass);
        logger.debug(message);
    }

    public static void info(final String message) {
        _logger.info(message);
    }

    public static void info(final Class<?> callingClass, final String message) {
        final Logger logger = LoggerFactory.getLogger(callingClass);
        logger.info(message);
    }

    public static void warn(final String message) {
        _logger.warn(message);
    }

    public static void warn(final Class<?> callingClass, final String message) {
        final Logger logger = LoggerFactory.getLogger(callingClass);
        logger.warn(message);
    }

    public static void warn(final String message, final Exception exception) {
        _logger.warn(message, exception);
    }

    public static void warn(final Class<?> callingClass, final String message, final Exception exception) {
        final Logger logger = LoggerFactory.getLogger(callingClass);
        logger.warn(message, exception);
    }

    public static void error(final String message) {
        _logger.error(message);
    }

    public static void error(final Class<?> callingClass, final String message) {
        final Logger logger = LoggerFactory.getLogger(callingClass);
        logger.error(message);
    }

    public static void error(final String message, final Exception exception) {
        _logger.error(message, exception);
    }

    public static void error(final Class<?> callingClass, final String message, final Exception exception) {
        final Logger logger = LoggerFactory.getLogger(callingClass);
        logger.error(message, exception);
    }
}
