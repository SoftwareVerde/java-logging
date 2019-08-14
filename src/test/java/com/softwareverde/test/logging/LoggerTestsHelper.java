package com.softwareverde.test.logging;

import com.softwareverde.logging.Logger;

public class LoggerTestsHelper {
    public static void debug(final String message) {
        Logger.debug(message);
    }

    public static void info(final String message) {
        Logger.info(message);
    }

    public static void warn(final String message) {
        Logger.warn(message);
    }

    public static void error(final String message) {
        Logger.error(message);
    }

    protected LoggerTestsHelper() { }
}
