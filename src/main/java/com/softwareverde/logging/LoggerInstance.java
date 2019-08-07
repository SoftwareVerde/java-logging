package com.softwareverde.logging;

public class LoggerInstance {
    protected final Class<?> _class;

    public LoggerInstance(final Class<?> clazz) {
        _class = clazz;
    }

    public void trace(final Object message) {
        Logger.log(LogLevel.TRACE, _class, Logger.stringify(message), null);
    }

    public void trace(final Throwable exception) {
        Logger.log(LogLevel.TRACE, _class, null, exception);
    }

    public void trace(final Object message, final Throwable exception) {
        Logger.log(LogLevel.TRACE, _class, Logger.stringify(message), exception);
    }

    public void debug(final Object message) {
        Logger.log(LogLevel.DEBUG, _class, Logger.stringify(message), null);
    }

    public void debug(final Throwable exception) {
        Logger.log(LogLevel.DEBUG, _class, null, exception);
    }

    public void debug(final Object message, final Throwable exception) {
        Logger.log(LogLevel.DEBUG, _class, Logger.stringify(message), exception);
    }

    public void info(final Object message) {
        Logger.log(LogLevel.INFO, _class, Logger.stringify(message), null);
    }

    public void info(final Throwable exception) {
        Logger.log(LogLevel.INFO, _class, null, exception);
    }

    public void info(final Object message, final Throwable exception) {
        Logger.log(LogLevel.INFO, _class, Logger.stringify(message), exception);
    }

    public void warn(final Object message) {
        Logger.log(LogLevel.WARN, _class, Logger.stringify(message), null);
    }

    public void warn(final Throwable exception) {
        Logger.log(LogLevel.WARN, _class, null, exception);
    }

    public void warn(final Object message, final Throwable exception) {
        Logger.log(LogLevel.WARN, _class, Logger.stringify(message), exception);
    }

    public void error(final Object message) {
        Logger.log(LogLevel.ERROR, _class, Logger.stringify(message), null);
    }

    public void error(final Throwable exception) {
        Logger.log(LogLevel.ERROR, _class, null, exception);
    }

    public void error(final Object message, final Throwable exception) {
        Logger.log(LogLevel.ERROR, _class, Logger.stringify(message), exception);
    }
}
