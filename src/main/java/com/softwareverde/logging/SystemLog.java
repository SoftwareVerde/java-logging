package com.softwareverde.logging;

public class SystemLog implements Log {
    protected static final Object INSTANCE_MUTEX = new Object();
    protected static volatile SystemLog INSTANCE = null;
    public static SystemLog getInstance() {
        if (INSTANCE == null) {
            synchronized (INSTANCE_MUTEX) {
                if (INSTANCE == null) {
                    INSTANCE = new SystemLog();
                }
            }
        }

        return INSTANCE;
    }

    protected SystemLog() { }

    @Override
    public synchronized void write(final LogLevel logLevel, final String message, final Throwable exception) {
        switch (logLevel) {
            case OFF: { } break;

            case DEBUG:
            case INFO: {
                if (message != null) {
                    System.out.println(message);
                }
                if (exception != null) {
                    exception.printStackTrace(System.out);
                }
            } break;

            case WARN:
            case ERROR: {
                if (message != null) {
                    System.err.println(message);
                }
                if (exception != null) {
                    exception.printStackTrace(System.err);
                }
            } break;
        }
    }
}
