package com.softwareverde.logging;

import com.softwareverde.util.Package;

class StackTraceManager extends java.lang.SecurityManager {
    public static final String LOGGING_PACKAGE_NAME = "com.softwareverde.logging";

    protected Class<?>[] _getCallingClasses() {
        { // Default case...
            final Class<?>[] callingClasses = super.getClassContext();
            if (callingClasses != null) { return callingClasses; }
        }

        final Exception exception = new Exception();
        final StackTraceElement[] stackTraceElements = exception.getStackTrace();
        final Class<?>[] callingClasses = new Class<?>[stackTraceElements.length];

        for (int i = 0; i < stackTraceElements.length; ++i) {
            final StackTraceElement stackTraceElement = stackTraceElements[i];
            final String className = stackTraceElement.getClassName();
            try {
                final Class<?> clazz = Class.forName(className);
                callingClasses[i] = clazz;
            }
            catch (final Exception classNotFoundException) {
                _printError("Class not found: " + className, null);
                callingClasses[i] = null;
            }
        }

        return callingClasses;
    }

    /**
     * Returns the first non-com.softwareverde.logging class invocation or, if such a class could not be identified, Logger.class.
     */
    public final Class<?> getCallingClass() {
        try {
            final Class<?>[] callingClasses = _getCallingClasses();

            for (int i = 0; i < callingClasses.length; ++i) {
                final Class<?> callingClass = callingClasses[i];
                if (callingClass != null) {
                    final String packageName = Package.getClassName(callingClass);
                    if (! packageName.startsWith(LOGGING_PACKAGE_NAME)) {
                        return callingClass;
                    }
                }
            }

            final Class<?> topLevelClass = callingClasses[callingClasses.length - 1];
            if (topLevelClass != null) {
                return callingClasses[callingClasses.length - 1];
            }
            return Logger.class;
        }
        catch (final Exception exception) {
            _printError("Unable to determine calling class", exception);
            return Logger.class;
        }
    }

    /**
     * Returns the call-depth of the first non-com.softwareverde.logging class invocation.
     *  The returned depth is from the perspective of the invoker of this function, not from within this function.
     */
    public final Integer getCallingDepth() {
        final Class<?>[] callingClasses = super.getClassContext();
        if (callingClasses.length < 2) { return 0; }

        for (int i = 1; i < callingClasses.length; ++i) {
            final Class<?> callingClass = callingClasses[i];
            if (callingClass != null) {
                final String packageName = Package.getClassName(callingClass);
                if (!packageName.startsWith(LOGGING_PACKAGE_NAME)) {
                    return (i - 1);
                }
            }
        }

        return (callingClasses.length - 2);
    }

    private void _printError(final String message, final Throwable throwable) {
        Logger.printLoggingError(LogLevel.ERROR, this.getClass(), message, throwable);
    }
}
