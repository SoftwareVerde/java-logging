package com.softwareverde.logging.log;

import com.softwareverde.logging.LogLevel;
import com.softwareverde.util.Package;

public class AnnotatedLog extends AbstractLog {
    protected static final Object INSTANCE_MUTEX = new Object();
    protected static volatile AnnotatedLog INSTANCE = null;
    public static AnnotatedLog getInstance() {
        if (INSTANCE == null) {
            synchronized (INSTANCE_MUTEX) {
                if (INSTANCE == null) {
                    INSTANCE = new AnnotatedLog(
                        SystemLog.wrapSystemStream(System.out),
                        SystemLog.wrapSystemStream(System.err)
                    );
                }
            }
        }

        return INSTANCE;
    }

    protected static volatile AnnotatedLog BUFFERED_INSTANCE = null;
    public static AnnotatedLog getBufferedInstance() {
        if (BUFFERED_INSTANCE == null) {
            synchronized (INSTANCE_MUTEX) {
                if (BUFFERED_INSTANCE == null) {
                    BUFFERED_INSTANCE = new AnnotatedLog(
                        new BufferedSystemWriter(BufferedSystemWriter.Type.SYSTEM_OUT),
                        new BufferedSystemWriter(BufferedSystemWriter.Type.SYSTEM_ERR)
                    );
                }
            }
        }

        return BUFFERED_INSTANCE;
    }

    protected static final String EMPTY_STRING = "";
    protected static final String SEPARATOR = " ";
    protected static final String SEGMENT_LEFT = "[";
    protected static final String SEGMENT_RIGHT = "]";

    protected String _getLogLevelAnnotation(final LogLevel logLevel) {
        return EMPTY_STRING;
    }

    protected String _getTimestampAnnotation() {
        return String.format("%.3f", (System.currentTimeMillis() / 1000D));
    }

    protected String _getClassAnnotation(final Class<?> callingClass) {
        return Package.getClassName(callingClass);
    }

    @Override
    protected void _writeMessage(final Class<?> callingClass, final LogLevel logLevel, final String message, final Writer writer) {
        final String timestampAnnotation = _getTimestampAnnotation();
        final String logLevelAnnotation = _getLogLevelAnnotation(logLevel);
        final String classAnnotation = _getClassAnnotation(callingClass);

        final StringBuilder stringBuilder = new StringBuilder();

        String separator = EMPTY_STRING;
        if (! timestampAnnotation.isEmpty()) {
            stringBuilder.append(separator);
            stringBuilder.append(SEGMENT_LEFT);
            stringBuilder.append(timestampAnnotation);
            stringBuilder.append(SEGMENT_RIGHT);
            separator = SEPARATOR;
        }

        if (! logLevelAnnotation.isEmpty()) {
            stringBuilder.append(separator);
            stringBuilder.append(SEGMENT_LEFT);
            stringBuilder.append(logLevelAnnotation);
            stringBuilder.append(SEGMENT_RIGHT);
            separator = SEPARATOR;
        }

        if (! classAnnotation.isEmpty()) {
            stringBuilder.append(separator);
            stringBuilder.append(SEGMENT_LEFT);
            stringBuilder.append(classAnnotation);
            stringBuilder.append(SEGMENT_RIGHT);
            separator = SEPARATOR;
        }

        if (! message.isEmpty()) {
            stringBuilder.append(separator);
            stringBuilder.append(message);
            separator = SEPARATOR;
        }

        writer.write(stringBuilder.toString());
    }

    @Override
    protected void _writeException(final Class<?> callingClass, final LogLevel logLevel, final Throwable exception, final Writer writer) {
        super._writeException(callingClass, logLevel, exception, writer);
    }

    protected AnnotatedLog(final Writer outWriter, final Writer errWriter) {
        super(outWriter, errWriter);
    }

    @Override
    public synchronized void write(final Class<?> callingClass, final LogLevel logLevel, final String nullableMessage, final Throwable nullableException) {

        final String message;
        final Throwable exception;
        if ( (nullableMessage == null) && (nullableException != null) ) {
            message = EMPTY_STRING;
            exception = nullableException;
        }
        else {
            message = nullableMessage;
            exception = nullableException;
        }

        super.write(callingClass, logLevel, message, exception);
    }
}
