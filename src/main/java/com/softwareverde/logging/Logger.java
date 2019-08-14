package com.softwareverde.logging;

import com.softwareverde.logging.log.AnnotatedLog;
import com.softwareverde.util.Package;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Logger {
    protected static final String NULL = "null";

    public static LogLevel DEFAULT_LOG_LEVEL = LogLevel.INFO;
    public static Log LOG = AnnotatedLog.getInstance();

    public static LoggerInstance getInstance(final Class<?> clazz) {
        return new LoggerInstance(clazz);
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
        final Log log = Logger.LOG;
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

    public static void setLogLevel(final Class clazz, final LogLevel level) {
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
        final Log log = Logger.LOG;
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

final class StackTraceManager extends java.lang.SecurityManager {
    public static final String LOGGING_PACKAGE_NAME = "com.softwareverde.logging";

	public final Class<?> getCallingClass() {
        final Class<?>[] callingClasses = super.getClassContext();
        for (int i = 0; i < callingClasses.length; ++i) {
            final Class<?> callingClass = callingClasses[i];
            final String packageName = Package.getClassName(callingClass);
            if (! packageName.startsWith(LOGGING_PACKAGE_NAME)) {
                return callingClass;
            }
        }

		return callingClasses[callingClasses.length - 1];
	}
}
