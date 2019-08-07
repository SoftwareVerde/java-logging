package com.softwareverde.logging;

public class LoggerInstance {
    protected final Class<?> _class;

    public LoggerInstance(final Class<?> clazz) {
        _class = clazz;
    }

    public void trace(final String message) {
        Logger.log(LogLevel.TRACE, _class, message, null);
    }

    public void trace(final Throwable exception) {
        Logger.log(LogLevel.TRACE, _class, null, exception);
    }

    public void trace(final String message, final Throwable exception) {
        Logger.log(LogLevel.TRACE, _class, message, exception);
    }

    public void debug(final String message) {
        Logger.log(LogLevel.DEBUG, _class, message, null);
    }

    public void debug(final Throwable exception) {
        Logger.log(LogLevel.DEBUG, _class, null, exception);
    }

    public void debug(final String message, final Throwable exception) {
        Logger.log(LogLevel.DEBUG, _class, message, exception);
    }

    public void info(final String message) {
        Logger.log(LogLevel.INFO, _class, message, null);
    }

    public void info(final Throwable exception) {
        Logger.log(LogLevel.INFO, _class, null, exception);
    }

    public void info(final String message, final Throwable exception) {
        Logger.log(LogLevel.INFO, _class, message, exception);
    }

    public void warn(final String message) {
        Logger.log(LogLevel.WARN, _class, message, null);
    }

    public void warn(final Throwable exception) {
        Logger.log(LogLevel.WARN, _class, null, exception);
    }

    public void warn(final String message, final Throwable exception) {
        Logger.log(LogLevel.WARN, _class, message, exception);
    }

    public void error(final String message) {
        Logger.log(LogLevel.ERROR, _class, message, null);
    }

    public void error(final Throwable exception) {
        Logger.log(LogLevel.ERROR, _class, null, exception);
    }

    public void error(final String message, final Throwable exception) {
        Logger.log(LogLevel.ERROR, _class, message, exception);
    }
}
