package com.softwareverde.logging;

import com.softwareverde.logging.log.AnnotatedLog;
import com.softwareverde.logging.log.SystemLog;
import com.softwareverde.util.Package;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Logger {
    protected static final String NULL = "null";

    public static LogLevel DEFAULT_LOG_LEVEL = LogLevel.INFO;
    public static LogFactory DEFAULT_LOG_FACTORY = (clazz) -> AnnotatedLog.getInstance();

    private static LogFactory LOG_FACTORY = DEFAULT_LOG_FACTORY;

    /**
     * <p>Sets the log factory by providing a Log object that should always be used, regardless of the class context.</p>
     * @param log
     */
    public static void setLog(final Log log) {
        LOG_FACTORY = (clazz) -> log;
    }

    /**
     * <p>Sets the log factory to be used in future log statements.</p>
     * @param logFactory
     */
    public static void setLogFactory(final LogFactory logFactory) {
        LOG_FACTORY = logFactory;
    }

    public static void printLoggingError(final LogLevel logLevel, final Class<?> clazz, final String message) {
        Logger.printLoggingError(logLevel, clazz, message, null);
    }

    public static void printLoggingError(final LogLevel logLevel, final Class<?> clazz, final String message, final Throwable throwable) {
        if (Logger.isLogLevelEnabled(logLevel, clazz)) {
            if (message != null) {
                System.err.println("Log Error: " + message);
            }
            if (throwable != null) {
                throwable.printStackTrace(System.err);
            }
        }
    }

    public static LoggerInstance getInstance(final Class<?> clazz) {
        try {
            final Log log = LOG_FACTORY.newLog(clazz);
            final LoggerInstance loggerInstance = new LoggerInstance(log, clazz);
            return loggerInstance;
        }
        catch (final Exception exception) {
            Logger.printLoggingError(LogLevel.ERROR, Logger.class, "Unable to create log instance with provided class: " + clazz, exception);
            try {
                final Log fallbackLogger = LOG_FACTORY.newLog(Logger.class);
                final LoggerInstance loggerInstance = new LoggerInstance(fallbackLogger, Logger.class);
                return loggerInstance;
            }
            catch (final Exception exception2) {
                Logger.printLoggingError(LogLevel.ERROR, Logger.class, "Unable to create fallback logger", exception2);
                final Log systemLog = SystemLog.getInstance();
                return new LoggerInstance(systemLog, Logger.class);
            }
        }
    }

    protected static String stringify(final Object object) {
        return (object == null ? NULL : object.toString());
    }

    protected static final ReentrantReadWriteLock.ReadLock _readLock;
    protected static final ReentrantReadWriteLock.WriteLock _writeLock;
    static {
        final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
        _readLock = readWriteLock.readLock();
        _writeLock = readWriteLock.writeLock();
    }

    protected static final PackageLevel _rootPackage = PackageLevel.newRootPackage();
    protected static final StackTraceManager _stackTraceManager = new StackTraceManager();

    protected static void log(final LogLevel eventLogLevel, final Class<?> callingClass, final String nullableMessage, final Throwable nullableException) {
        final Log log = LOG_FACTORY.newLog(callingClass);
        Logger.log(log, eventLogLevel, callingClass, nullableMessage, nullableException);
    }

    protected static void log(final Log log, final LogLevel eventLogLevel, final Class<?> callingClass, final String nullableMessage, final Throwable nullableException) {
        if (log == null) { return; }

        final LogLevel classLogLevel;
        _readLock.lock();
        try {
            final LogLevel nullableClassLogLevel = _rootPackage.getLogLevel(Package.getClassName(callingClass));
            classLogLevel = (nullableClassLogLevel == null ? DEFAULT_LOG_LEVEL : nullableClassLogLevel);
        }
        finally {
            _readLock.unlock();
        }

        if ( (eventLogLevel == LogLevel.OFF) || (classLogLevel == LogLevel.OFF) ) { return; }
        if (eventLogLevel.value < classLogLevel.value) { return; }

        log.write(callingClass, eventLogLevel, nullableMessage, nullableException);
    }

    protected static Class<?> getCallingClass() {
        return _stackTraceManager.getCallingClass();
    }

    public static LogLevel getLogLevel() {
        final Class<?> callingClass = _stackTraceManager.getCallingClass();

        _readLock.lock();
        try {
            return _rootPackage.getLogLevel(Package.getClassName(callingClass));
        }
        finally {
            _readLock.unlock();
        }
    }

    public static LogLevel getLogLevel(final Class<?> callingClass) {
        _readLock.lock();
        try {
            return _rootPackage.getLogLevel(Package.getClassName(callingClass));
        }
        finally {
            _readLock.unlock();
        }
    }

    protected static boolean isLogLevelEnabled(final LogLevel loggingThreshold, final Class<?> callingClass) {
        LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        if (classLogLevel == null) {
            classLogLevel = DEFAULT_LOG_LEVEL;
        }
        // using opposite order, since we're testing the passed-in threshold against the class's log level
        return loggingThreshold.isLoggableWithThreshold(classLogLevel);
    }

    public static boolean isTraceEnabled() {
        final Class<?> callingClass = Logger.getCallingClass();
        return Logger.isLogLevelEnabled(LogLevel.TRACE, callingClass);
    }

    public static boolean isDebugEnabled() {
        final Class<?> callingClass = Logger.getCallingClass();
        return Logger.isLogLevelEnabled(LogLevel.DEBUG, callingClass);
    }

    public static boolean isInfoEnabled() {
        final Class<?> callingClass = Logger.getCallingClass();
        return Logger.isLogLevelEnabled(LogLevel.INFO, callingClass);
    }

    public static boolean isWarnEnabled() {
        final Class<?> callingClass = Logger.getCallingClass();
        return Logger.isLogLevelEnabled(LogLevel.WARN, callingClass);
    }

    public static boolean isErrorEnabled() {
        final Class<?> callingClass = Logger.getCallingClass();
        return Logger.isLogLevelEnabled(LogLevel.ERROR, callingClass);
    }

    public static void setLogLevel(final Class<?> clazz, final LogLevel level) {
        final PackageLevel packageLogLevel = PackageLevel.fromClass(clazz, level);

        _writeLock.lock();
        try {
            _rootPackage.mergeInPackage(packageLogLevel);
        }
        finally {
            _writeLock.unlock();
        }
    }

    public static void setLogLevel(final String packageName, final LogLevel level) {
        final PackageLevel packageLogLevel = PackageLevel.fromString(packageName, level);

        _writeLock.lock();
        try {
            _rootPackage.mergeInPackage(packageLogLevel);
        }
        finally {
            _writeLock.unlock();
        }
    }

    public static void clearLogLevels() {
        _writeLock.lock();
        try {
            _rootPackage.clear();
        }
        finally {
            _writeLock.unlock();
        }
    }

    /**
     * Flushes the log, if the log is buffered.
     *  In most cases, this operation does nothing.
     */
    public static void flush() {
        final Class<?> callingClass = Logger.getCallingClass();
        final Log log = Logger.LOG_FACTORY.newLog(callingClass);
        if (log == null) { return; }

        log.flush();
    }

    // TRACE

    public static void trace(final Object message) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.TRACE, callingClass, Logger.stringify(message), null);
    }

    public static void trace(final Throwable exception) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.TRACE, callingClass, null, exception);
    }

    public static void trace(final Object message, final Throwable exception) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.TRACE, callingClass, Logger.stringify(message), exception);
    }

    public static void trace(final Class<?> callingClass, final Object message) {
        Logger.log(LogLevel.TRACE, callingClass, Logger.stringify(message), null);
    }

    public static void trace(final Class<?> callingClass, final Throwable exception) {
        Logger.log(LogLevel.TRACE, callingClass, null, exception);
    }

    public static void trace(final Class<?> callingClass, final Object message, final Throwable exception) {
        Logger.log(LogLevel.TRACE, callingClass, Logger.stringify(message), exception);
    }

    // DEBUG

    public static void debug(final Object message) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.DEBUG, callingClass, Logger.stringify(message), null);
    }

    public static void debug(final Throwable exception) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.DEBUG, callingClass, null, exception);
    }

    public static void debug(final Object message, final Throwable exception) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.DEBUG, callingClass, Logger.stringify(message), exception);
    }

    public static void debug(final Class<?> callingClass, final Object message) {
        Logger.log(LogLevel.DEBUG, callingClass, Logger.stringify(message), null);
    }

    public static void debug(final Class<?> callingClass, final Throwable exception) {
        Logger.log(LogLevel.DEBUG, callingClass, null, exception);
    }

    public static void debug(final Class<?> callingClass, final Object message, final Throwable exception) {
        Logger.log(LogLevel.DEBUG, callingClass, Logger.stringify(message), exception);
    }

    // INFO

    public static void info(final Object message) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.INFO, callingClass, Logger.stringify(message), null);
    }

    public static void info(final Throwable exception) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.INFO, callingClass, null, exception);
    }

    public static void info(final Object message, final Throwable exception) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.INFO, callingClass, Logger.stringify(message), exception);
    }

    public static void info(final Class<?> callingClass, final Object message) {
        Logger.log(LogLevel.INFO, callingClass, Logger.stringify(message), null);
    }

    public static void info(final Class<?> callingClass, final Throwable exception) {
        Logger.log(LogLevel.INFO, callingClass, null, exception);
    }

    public static void info(final Class<?> callingClass, final Object message, final Throwable exception) {
        Logger.log(LogLevel.INFO, callingClass, Logger.stringify(message), exception);
    }

    // WARN

    public static void warn(final Object message) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.WARN, callingClass, Logger.stringify(message), null);
    }

    public static void warn(final Throwable exception) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.WARN, callingClass, null, exception);
    }

    public static void warn(final Object message, final Throwable exception) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.WARN, callingClass, Logger.stringify(message), exception);
    }

    public static void warn(final Class<?> callingClass, final Object message) {
        Logger.log(LogLevel.WARN, callingClass, Logger.stringify(message), null);
    }

    public static void warn(final Class<?> callingClass, final Throwable exception) {
        Logger.log(LogLevel.WARN, callingClass, null, exception);
    }

    public static void warn(final Class<?> callingClass, final Object message, final Throwable exception) {
        Logger.log(LogLevel.WARN, callingClass, Logger.stringify(message), exception);
    }

    // ERROR

    public static void error(final Object message) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.ERROR, callingClass, Logger.stringify(message), null);
    }

    public static void error(final Throwable exception) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.ERROR, callingClass, null, exception);
    }

    public static void error(final Object message, final Throwable exception) {
        final Class<?> callingClass = Logger.getCallingClass();
        Logger.log(LogLevel.ERROR, callingClass, Logger.stringify(message), exception);
    }

    public static void error(final Class<?> callingClass, final Object message) {
        Logger.log(LogLevel.ERROR, callingClass, Logger.stringify(message), null);
    }

    public static void error(final Class<?> callingClass, final Throwable exception) {
        Logger.log(LogLevel.ERROR, callingClass, null, exception);
    }

    public static void error(final Class<?> callingClass, final Object message, final Throwable exception) {
        Logger.log(LogLevel.ERROR, callingClass, Logger.stringify(message), exception);
    }

    protected Logger() { }
}
