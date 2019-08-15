package com.softwareverde.logging;

import com.softwareverde.util.Package;

class StackTraceManager extends java.lang.SecurityManager {
    public static final String LOGGING_PACKAGE_NAME = "com.softwareverde.logging";

    /**
     * Returns the first non-com.softwareverde.logging class invocation.
     */
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

    /**
     * Returns the call-depth of the first non-com.softwareverde.logging class invocation.
     */
    public final Integer getCallingDepth() {
        final Class<?>[] callingClasses = super.getClassContext();
        for (int i = 0; i < callingClasses.length; ++i) {
            final Class<?> callingClass = callingClasses[i];
            final String packageName = Package.getClassName(callingClass);
            if (! packageName.startsWith(LOGGING_PACKAGE_NAME)) {
                return i;
            }
        }

        return (callingClasses.length - 1);
    }
}
