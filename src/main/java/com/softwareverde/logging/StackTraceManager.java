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
     *  The returned depth is from the perspective of the invoker of this function, not from within this function.
     */
    public final Integer getCallingDepth() {
        final Class<?>[] callingClasses = super.getClassContext();
        if (callingClasses.length < 2) { return 0; }

        for (int i = 1; i < callingClasses.length; ++i) {
            final Class<?> callingClass = callingClasses[i];
            final String packageName = Package.getClassName(callingClass);
            if (! packageName.startsWith(LOGGING_PACKAGE_NAME)) {
                return (i - 1);
            }
        }

        return (callingClasses.length - 2);
    }
}
