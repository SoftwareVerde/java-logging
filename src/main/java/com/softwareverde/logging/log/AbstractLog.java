package com.softwareverde.logging.log;

import com.softwareverde.logging.Log;
import com.softwareverde.logging.LogLevel;

/**
 * Log statements are written to System.out/System.err.
 *  WARN/ERROR are written to the _errWriter; other statements are written to _outWriter.
 */
public abstract class AbstractLog implements Log {
    public interface Writer {
        void write(String string);
        void write(Throwable exception);
        default void flush() { }
    }

    protected final Writer _outWriter;
    protected final Writer _errWriter;
    protected String _lineSeparator = System.lineSeparator();

    protected void _flush() {
        _outWriter.flush();
        _errWriter.flush();
    }

    protected void _writeMessage(final Class<?> callingClass, final LogLevel logLevel, final String message, final Writer writer) {
        writer.write(message + _lineSeparator);
    }

    protected void _writeException(final Class<?> callingClass, final LogLevel logLevel, final Throwable exception, final Writer writer) {
        writer.write(exception);
    }

    protected AbstractLog(final Writer outWriter, final Writer errWriter) {
        _outWriter = outWriter;
        _errWriter = errWriter;
    }

    @Override
    public synchronized void write(final Class<?> callingClass, final LogLevel logLevel, final String message, final Throwable exception) {
        switch (logLevel) {
            case OFF: { } break;

            case TRACE:
            case DEBUG:
            case INFO: {
                if (message != null) {
                    _writeMessage(callingClass, logLevel, message, _outWriter);
                }
                if (exception != null) {
                    _writeException(callingClass, logLevel, exception, _outWriter);
                }
            } break;

            case WARN:
            case ERROR: {
                if (message != null) {
                    _writeMessage(callingClass, logLevel, message, _errWriter);
                }
                if (exception != null) {
                    _writeException(callingClass, logLevel, exception, _errWriter);
                }
            } break;
        }
    }

    /**
     * Does nothing.
     */
    @Override
    public void flush() { }

    @Override
    protected void finalize() throws Throwable {
        _flush();
        super.finalize();
    }
}
