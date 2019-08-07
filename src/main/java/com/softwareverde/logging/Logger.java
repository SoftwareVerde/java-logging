package com.softwareverde.logging;

public class Logger {

    public static LogLevel DEFAULT_LOG_LEVEL = LogLevel.WARN;
    public static Log LOG = SystemLog.getInstance();

    public static LoggerInstance getInstance(final Class<?> clazz) {
        return new LoggerInstance(clazz);
    }

    protected static final PackageLevel _rootPackage = PackageLevel.newRootPackage();
    protected static final StackTraceManager _stackTraceManager = new StackTraceManager();

    protected static void log(final LogLevel eventLogLevel, final LogLevel nullableClassLogLevel, final String nullableMessage, final Throwable nullableException) {
        final Log log = Logger.LOG;
        if (log == null) { return; }

        final LogLevel classLogLevel = (nullableClassLogLevel == null ? DEFAULT_LOG_LEVEL : nullableClassLogLevel);

        if ( (eventLogLevel == LogLevel.OFF) || (classLogLevel == LogLevel.OFF) ) { return; }
        if (eventLogLevel.value < classLogLevel.value) { return; }

        log.write(eventLogLevel, nullableMessage, nullableException);
    }

    protected static LogLevel getLogLevel() {
        final Class<?> callingClass = _stackTraceManager.getCallingClass(StackTraceManager.Offset.PARENT + 1);
        return _rootPackage.getLogLevel(callingClass.getCanonicalName());
    }

    protected static LogLevel getLogLevel(final Class<?> callingClass) {
        return _rootPackage.getLogLevel(callingClass.getCanonicalName());
    }

    public static void setLogLevel(final Class clazz, final LogLevel level) {
        final PackageLevel packageLogLevel = PackageLevel.fromClass(clazz, level);
        _rootPackage.mergeInPackage(packageLogLevel);
    }

    public static void setLogLevel(final String packageName, final LogLevel level) {
        final PackageLevel packageLogLevel = PackageLevel.fromString(packageName, level);
        _rootPackage.mergeInPackage(packageLogLevel);
    }

    public static void clearLogLevels() {
        _rootPackage.clear();
    }

    // DEBUG

    public static void debug(final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.DEBUG, classLogLevel, message, null);
    }

    public static void debug(final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.DEBUG, classLogLevel, null, exception);
    }

    public static void debug(final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.DEBUG, classLogLevel, message, exception);
    }

    public static void debug(final Class<?> callingClass, final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.DEBUG, classLogLevel, message, null);
    }

    public static void debug(final Class<?> callingClass, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.DEBUG, classLogLevel, null, exception);
    }

    public static void debug(final Class<?> callingClass, final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.DEBUG, classLogLevel, message, exception);
    }

    // INFO

    public static void info(final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.INFO, classLogLevel, message, null);
    }

    public static void info(final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.INFO, classLogLevel, null, exception);
    }

    public static void info(final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.INFO, classLogLevel, message, exception);
    }

    public static void info(final Class<?> callingClass, final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.INFO, classLogLevel, message, null);
    }

    public static void info(final Class<?> callingClass, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.INFO, classLogLevel, null, exception);
    }

    public static void info(final Class<?> callingClass, final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.INFO, classLogLevel, message, exception);
    }

    // WARN

    public static void warn(final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.WARN, classLogLevel, message, null);
    }

    public static void warn(final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.WARN, classLogLevel, null, exception);
    }

    public static void warn(final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.WARN, classLogLevel, message, exception);
    }

    public static void warn(final Class<?> callingClass, final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.WARN, classLogLevel, message, null);
    }

    public static void warn(final Class<?> callingClass, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.WARN, classLogLevel, null, exception);
    }

    public static void warn(final Class<?> callingClass, final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.WARN, classLogLevel, message, exception);
    }

    // ERROR

    public static void error(final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.ERROR, classLogLevel, message, null);
    }

    public static void error(final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.ERROR, classLogLevel, null, exception);
    }

    public static void error(final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel();
        Logger.log(LogLevel.ERROR, classLogLevel, message, exception);
    }

    public static void error(final Class<?> callingClass, final String message) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.ERROR, classLogLevel, message, null);
    }

    public static void error(final Class<?> callingClass, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.ERROR, classLogLevel, null, exception);
    }

    public static void error(final Class<?> callingClass, final String message, final Throwable exception) {
        final LogLevel classLogLevel = Logger.getLogLevel(callingClass);
        Logger.log(LogLevel.ERROR, classLogLevel, message, exception);
    }
}

final class StackTraceManager extends java.lang.SecurityManager {
    public static class Offset {
        public static final int THIS = 1;
        public static final int PARENT = 2;
    }

	public final Class<?> getCallingClass() {
		return super.getClassContext()[Offset.PARENT];
	}

	public final Class<?> getCallingClass(final int depth) {
		return super.getClassContext()[depth];
	}
}
