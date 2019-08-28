package com.softwareverde.logging.log;

import java.io.PrintStream;

/**
 * Log statements are written to System.out/System.err.
 *  WARN/ERROR are written to standard error; other statements are written to standard out.
 */
public class SystemLog extends AbstractLog {
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

    protected static volatile SystemLog BUFFERED_INSTANCE = null;
    public static SystemLog getBufferedInstance() {
        if (BUFFERED_INSTANCE == null) {
            synchronized (INSTANCE_MUTEX) {
                if (BUFFERED_INSTANCE == null) {
                    BUFFERED_INSTANCE = new SystemLog(
                        new BufferedSystemWriter(BufferedSystemWriter.Type.SYSTEM_OUT),
                        new BufferedSystemWriter(BufferedSystemWriter.Type.SYSTEM_ERR)
                    );
                }
            }
        }

        return BUFFERED_INSTANCE;
    }

    public static Writer wrapSystemStream(final PrintStream printStream) {
        return new Writer() {
            @Override
            public void write(final String string) {
                printStream.print(string); // any desired newlines should be a part of the string
            }

            @Override
            public void write(final Throwable exception) {
                exception.printStackTrace(printStream);
            }
        };
    }

    protected SystemLog() {
        super(SystemLog.wrapSystemStream(System.out), SystemLog.wrapSystemStream(System.err));
    }

    protected SystemLog(final Writer outWriter, final Writer errWriter) {
        super(outWriter, errWriter);
    }
}
