package com.softwareverde.logging;

public class LoggerInstance {
    protected final Log _log;
    protected final Class<?> _class;

    public LoggerInstance(final Log log, final Class<?> clazz) {
        _log = log;
        _class = clazz;
    }

    public boolean isTraceEnabled() {
        return Logger.isLogLevelEnabled(LogLevel.TRACE, _class);
    }

    public boolean isDebugEnabled() {
        return Logger.isLogLevelEnabled(LogLevel.DEBUG, _class);
    }

    public boolean isInfoEnabled() {
        return Logger.isLogLevelEnabled(LogLevel.INFO, _class);
    }

    public boolean isWarnEnabled() {
        return Logger.isLogLevelEnabled(LogLevel.WARN, _class);
    }

    public boolean isErrorEnabled() {
        return Logger.isLogLevelEnabled(LogLevel.ERROR, _class);
    }

    public void log(final LogLevel logLevel, final Object message) {
        Logger.log(_log, logLevel, _class, Logger.stringify(message), null);
    }

    public void log(final LogLevel logLevel, final Throwable exception) {
        Logger.log(_log, logLevel, _class, null, exception);
    }

    public void log(final LogLevel logLevel, final String message, final Throwable exception) {
        Logger.log(_log, logLevel, _class, Logger.stringify(message), exception);
    }

    public void trace(final Object message) {
        Logger.log(_log, LogLevel.TRACE, _class, Logger.stringify(message), null);
    }

    public void trace(final Throwable exception) {
        Logger.log(_log, LogLevel.TRACE, _class, null, exception);
    }

    public void trace(final Object message, final Throwable exception) {
        Logger.log(_log, LogLevel.TRACE, _class, Logger.stringify(message), exception);
    }

    public void debug(final Object message) {
        Logger.log(_log, LogLevel.DEBUG, _class, Logger.stringify(message), null);
    }

    public void debug(final Throwable exception) {
        Logger.log(_log, LogLevel.DEBUG, _class, null, exception);
    }

    public void debug(final Object message, final Throwable exception) {
        Logger.log(_log, LogLevel.DEBUG, _class, Logger.stringify(message), exception);
    }

    public void info(final Object message) {
        Logger.log(_log, LogLevel.INFO, _class, Logger.stringify(message), null);
    }

    public void info(final Throwable exception) {
        Logger.log(_log, LogLevel.INFO, _class, null, exception);
    }

    public void info(final Object message, final Throwable exception) {
        Logger.log(_log, LogLevel.INFO, _class, Logger.stringify(message), exception);
    }

    public void warn(final Object message) {
        Logger.log(_log, LogLevel.WARN, _class, Logger.stringify(message), null);
    }

    public void warn(final Throwable exception) {
        Logger.log(_log, LogLevel.WARN, _class, null, exception);
    }

    public void warn(final Object message, final Throwable exception) {
        Logger.log(_log, LogLevel.WARN, _class, Logger.stringify(message), exception);
    }

    public void error(final Object message) {
        Logger.log(_log, LogLevel.ERROR, _class, Logger.stringify(message), null);
    }

    public void error(final Throwable exception) {
        Logger.log(_log, LogLevel.ERROR, _class, null, exception);
    }

    public void error(final Object message, final Throwable exception) {
        Logger.log(_log, LogLevel.ERROR, _class, Logger.stringify(message), exception);
    }
}
