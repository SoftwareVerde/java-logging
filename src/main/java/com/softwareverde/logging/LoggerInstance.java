package com.softwareverde.logging;

public class LoggerInstance {
    protected final Class<?> _class;

    public LoggerInstance(final Class<?> clazz) {
        _class = clazz;
    }

    public void debug(final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.DEBUG, classLogLevel, message, null);
    }

    public void debug(final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.DEBUG, classLogLevel, null, exception);
    }

    public void debug(final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.DEBUG, classLogLevel, message, exception);
    }

    public void info(final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.INFO, classLogLevel, message, null);
    }

    public void info(final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.INFO, classLogLevel, null, exception);
    }

    public void info(final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.INFO, classLogLevel, message, exception);
    }

    public void warn(final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.WARN, classLogLevel, message, null);
    }

    public void warn(final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.WARN, classLogLevel, null, exception);
    }

    public void warn(final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.WARN, classLogLevel, message, exception);
    }

    public void error(final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.ERROR, classLogLevel, message, null);
    }

    public void error(final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.ERROR, classLogLevel, null, exception);
    }

    public void error(final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(_class);
        Logger.log(LogLevel.ERROR, classLogLevel, message, exception);
    }
}
