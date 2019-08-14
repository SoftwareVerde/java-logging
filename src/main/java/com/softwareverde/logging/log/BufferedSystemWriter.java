package com.softwareverde.logging.log;

import java.io.*;
import java.nio.charset.Charset;

public class BufferedSystemWriter implements AbstractLog.Writer {
    public enum Type {
        SYSTEM_OUT, SYSTEM_ERR
    }

    private static PrintWriter wrapStream(final FileDescriptor fileDescriptor) {
        return new PrintWriter(
            new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(fileDescriptor),
                    Charset.forName("UTF-8")
                ),
                512
            )
        );
    }

    protected final Type _type;
    protected final PrintWriter _printWriter;

    public BufferedSystemWriter(final Type type) {
        _type = type;
        _printWriter = BufferedSystemWriter.wrapStream((type == Type.SYSTEM_OUT) ? FileDescriptor.out : FileDescriptor.err);
    }

    @Override
    public void write(final String string) {
        _printWriter.write(string);
    }

    @Override
    public void write(final Throwable exception) {
        exception.printStackTrace(_printWriter);
    }

    @Override
    public void flush() {
        _printWriter.flush();
    }
}
