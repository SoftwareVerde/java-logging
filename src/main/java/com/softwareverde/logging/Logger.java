package com.softwareverde.logging;

public class Logger {
    public enum Level {
        OFF, DEBUG, INFO, WARN, ERROR
    }

    public static void setLevel(final Class clazz, final Level level) {

    }

    public static void debug(final String message) {

    }

    public static void debug(final Class<?> callingClass, final String message) {

    }

    public static void info(final String message) {

    }

    public static void info(final Class<?> callingClass, final String message) {

    }

    public static void warn(final String message) {

    }

    public static void warn(final Class<?> callingClass, final String message) {

    }

    public static void warn(final String message, final Exception exception) {

    }

    public static void warn(final Class<?> callingClass, final String message, final Exception exception) {

    }

    public static void error(final String message) {

    }

    public static void error(final Class<?> callingClass, final String message) {

    }

    public static void error(final String message, final Exception exception) {

    }

    public static void error(final Class<?> callingClass, final String message, final Exception exception) {

    }
}
