package com.softwareverde.logging;

import com.softwareverde.logging.log.AnnotatedLog;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class LoggerTests {
    public static class DebugLog implements Log {
        public static class Message {
            public final LogLevel logLevel;
            public final String message;
            public final Throwable exception;

            public Message(final LogLevel logLevel, final String message, final Throwable exception) {
                this.logLevel = logLevel;
                this.message = message;
                this.exception = exception;
            }
        }

        protected final ArrayList<Message> _messages = new ArrayList<Message>();

        @Override
        public void write(final Class<?> callingClass, final LogLevel logLevel, final String nullableMessage, final Throwable nullableException) {
            _messages.add(new Message(logLevel, nullableMessage, nullableException));
        }

        public List<Message> getMessages() {
            return _messages;
        }
    }

    public static class AnnotatedDebugLog extends AnnotatedLog {
        protected static class ListWriter implements Writer {
            List<String> _messages;

            @Override
            public void write(final String string) {
                _messages.add(string);
            }

            @Override
            public void write(final Throwable exception) {
                final StringWriter stringWriter = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(stringWriter);
                exception.printStackTrace(printWriter);

                _messages.add(stringWriter.toString());
            }

            public void setList(final List<String> messages) {
                _messages = messages;
            }
        }

        protected final ArrayList<String> _messages = new ArrayList<String>();

        public AnnotatedDebugLog() {
            super(new ListWriter(), new ListWriter());
            ((ListWriter) _outWriter).setList(_messages);
            ((ListWriter) _errWriter).setList(_messages);
        }

        public List<String> getMessages() {
            return _messages;
        }
    }

    public static final LogLevel ORIGINAL_LOG_LEVEL = Logger.DEFAULT_LOG_LEVEL;
    public static final Log ORIGINAL_LOG = Logger.LOG;

    @Before
    public void setUp() {
        Logger.DEFAULT_LOG_LEVEL = ORIGINAL_LOG_LEVEL;
        Logger.LOG = ORIGINAL_LOG;
        Logger.clearLogLevels();
    }

    @After
    public void tearDown() {
        Logger.DEFAULT_LOG_LEVEL = ORIGINAL_LOG_LEVEL;
        Logger.LOG = ORIGINAL_LOG;
        Logger.clearLogLevels();
    }

    @Test
    public void should_log_messages_with_debug_default_level() {
        // Setup
        final DebugLog debugLog = new DebugLog();
        Logger.LOG = debugLog;
        Logger.DEFAULT_LOG_LEVEL = LogLevel.DEBUG;

        // Action
        Logger.debug("DEBUG");
        Logger.info("INFO");
        Logger.warn("WARN");
        Logger.error("ERROR");

        // Assert
        final List<DebugLog.Message> messages = debugLog.getMessages();

        Assert.assertEquals(4, messages.size());

        Assert.assertEquals("DEBUG", messages.get(0).message);
        Assert.assertEquals(LogLevel.DEBUG, messages.get(0).logLevel);

        Assert.assertEquals("INFO", messages.get(1).message);
        Assert.assertEquals(LogLevel.INFO, messages.get(1).logLevel);

        Assert.assertEquals("WARN", messages.get(2).message);
        Assert.assertEquals(LogLevel.WARN, messages.get(2).logLevel);

        Assert.assertEquals("ERROR", messages.get(3).message);
        Assert.assertEquals(LogLevel.ERROR, messages.get(3).logLevel);
    }

    @Test
    public void should_not_log_messages_below_default_level() {
        // Setup
        final DebugLog debugLog = new DebugLog();
        Logger.LOG = debugLog;
        Logger.DEFAULT_LOG_LEVEL = LogLevel.WARN;

        // Action
        Logger.debug("DEBUG");
        Logger.info("INFO");
        Logger.warn("WARN");
        Logger.error("ERROR");

        // Assert
        final List<DebugLog.Message> messages = debugLog.getMessages();

        Assert.assertEquals(2, messages.size());

        Assert.assertEquals("WARN", messages.get(0).message);
        Assert.assertEquals(LogLevel.WARN, messages.get(0).logLevel);

        Assert.assertEquals("ERROR", messages.get(1).message);
        Assert.assertEquals(LogLevel.ERROR, messages.get(1).logLevel);
    }

    @Test
    public void should_not_log_messages_with_OFF_default_level() {
        // Setup
        final DebugLog debugLog = new DebugLog();
        Logger.LOG = debugLog;
        Logger.DEFAULT_LOG_LEVEL = LogLevel.OFF;

        // Action
        Logger.debug("DEBUG");
        Logger.info("INFO");
        Logger.warn("WARN");
        Logger.error("ERROR");

        // Assert
        final List<DebugLog.Message> messages = debugLog.getMessages();

        Assert.assertEquals(0, messages.size());
    }

    @Test
    public void should_log_messages_if_package_set_above_default_level() {
        // Setup
        final DebugLog debugLog = new DebugLog();
        Logger.LOG = debugLog;
        Logger.DEFAULT_LOG_LEVEL = LogLevel.WARN;

        Logger.setLogLevel(LoggerTests.class, LogLevel.DEBUG);

        // Action
        Logger.debug("DEBUG");
        Logger.info("INFO");
        Logger.warn("WARN");
        Logger.error("ERROR");

        // Assert
        final List<DebugLog.Message> messages = debugLog.getMessages();

        Assert.assertEquals(4, messages.size());

        Assert.assertEquals("DEBUG", messages.get(0).message);
        Assert.assertEquals(LogLevel.DEBUG, messages.get(0).logLevel);

        Assert.assertEquals("INFO", messages.get(1).message);
        Assert.assertEquals(LogLevel.INFO, messages.get(1).logLevel);

        Assert.assertEquals("WARN", messages.get(2).message);
        Assert.assertEquals(LogLevel.WARN, messages.get(2).logLevel);

        Assert.assertEquals("ERROR", messages.get(3).message);
        Assert.assertEquals(LogLevel.ERROR, messages.get(3).logLevel);
    }

    @Test
    public void should_log_messages_if_separate_classes_have_different_log_levels() {
        // Setup
        final DebugLog thisDebugLog = new DebugLog();
        final DebugLog helperDebugLog = new DebugLog();
        Logger.DEFAULT_LOG_LEVEL = LogLevel.WARN;

        Logger.setLogLevel(LoggerTests.class, LogLevel.DEBUG);
        Logger.setLogLevel(LoggerTestsHelper.class, LogLevel.WARN);

        // Action
        Logger.LOG = thisDebugLog;
        {
            Logger.debug("THIS DEBUG");
            Logger.info("THIS INFO");
            Logger.warn("THIS WARN");
            Logger.error("THIS ERROR");
        }

        Logger.LOG = helperDebugLog;
        {
            LoggerTestsHelper.debug("HELPER DEBUG");
            LoggerTestsHelper.info("HELPER INFO");
            LoggerTestsHelper.warn("HELPER WARN");
            LoggerTestsHelper.error("HELPER ERROR");
        }

        // Assert
        final List<DebugLog.Message> thisMessages = thisDebugLog.getMessages();
        {
            Assert.assertEquals(4, thisMessages.size());

            Assert.assertEquals("THIS DEBUG", thisMessages.get(0).message);
            Assert.assertEquals(LogLevel.DEBUG, thisMessages.get(0).logLevel);

            Assert.assertEquals("THIS INFO", thisMessages.get(1).message);
            Assert.assertEquals(LogLevel.INFO, thisMessages.get(1).logLevel);

            Assert.assertEquals("THIS WARN", thisMessages.get(2).message);
            Assert.assertEquals(LogLevel.WARN, thisMessages.get(2).logLevel);

            Assert.assertEquals("THIS ERROR", thisMessages.get(3).message);
            Assert.assertEquals(LogLevel.ERROR, thisMessages.get(3).logLevel);
        }

        final List<DebugLog.Message> helperMessages = helperDebugLog.getMessages();
        {
            Assert.assertEquals(2, helperMessages.size());

            Assert.assertEquals("HELPER WARN", helperMessages.get(0).message);
            Assert.assertEquals(LogLevel.WARN, helperMessages.get(0).logLevel);

            Assert.assertEquals("HELPER ERROR", helperMessages.get(1).message);
            Assert.assertEquals(LogLevel.ERROR, helperMessages.get(1).logLevel);
        }
    }

    @Test
    public void should_log_messages_if_parent_package_has_lower_log_levels() {
        // Setup
        final DebugLog debugLog = new DebugLog();
        Logger.LOG = debugLog;
        Logger.DEFAULT_LOG_LEVEL = LogLevel.OFF;

        Logger.setLogLevel(LoggerTestsHelper.class, LogLevel.DEBUG); // NOTE: LoggerTestsHelper is not this class.
        Logger.setLogLevel("com.softwareverde", LogLevel.WARN);

        // Action
        Logger.debug("DEBUG");
        Logger.info("INFO");
        Logger.warn("WARN");
        Logger.error("ERROR");

        // Assert
        final List<DebugLog.Message> messages = debugLog.getMessages();

        Assert.assertEquals(2, messages.size());

        Assert.assertEquals("WARN", messages.get(0).message);
        Assert.assertEquals(LogLevel.WARN, messages.get(0).logLevel);

        Assert.assertEquals("ERROR", messages.get(1).message);
        Assert.assertEquals(LogLevel.ERROR, messages.get(1).logLevel);
    }

    static class StaticInnerClass {
        void log(final String message) {
            Logger.info(message);
        }
    }

    @Test
    public void should_log_messages_from_static_inner_class() {
        // Setup
        final AnnotatedDebugLog debugLog = new AnnotatedDebugLog();
        Logger.LOG = debugLog;
        Logger.DEFAULT_LOG_LEVEL = LogLevel.ON;

        final StaticInnerClass staticInnerClass = new StaticInnerClass();

        // Action
        staticInnerClass.log("Message.");

        // Assert
        final List<String> messages = debugLog.getMessages();

        Assert.assertEquals(1, messages.size());

        final String message = messages.get(0);
        Assert.assertTrue(message.endsWith(" [com.softwareverde.logging.LoggerTests.StaticInnerClass] Message."));
    }

    @Test
    public void should_log_messages_from_anonymous_class() {
        // Setup
        final AnnotatedDebugLog debugLog = new AnnotatedDebugLog();
        Logger.LOG = debugLog;
        Logger.DEFAULT_LOG_LEVEL = LogLevel.ON;

        // Action
        (new Runnable() {
            @Override
            public void run() {
                Logger.info("Message.");
            }
        }).run();

        // Assert
        final List<String> messages = debugLog.getMessages();

        Assert.assertEquals(1, messages.size());

        final String message = messages.get(0);
        Assert.assertTrue(message.endsWith(" [com.softwareverde.logging.LoggerTests] Message."));
    }

    @Test
    public void should_log_messages_from_lambda() {
        // Setup
        final AnnotatedDebugLog debugLog = new AnnotatedDebugLog();
        Logger.LOG = debugLog;
        Logger.DEFAULT_LOG_LEVEL = LogLevel.ON;

        // Action
        final Runnable lambda = (() -> {
            Logger.info("Message.");
        });
        lambda.run();

        // Assert
        final List<String> messages = debugLog.getMessages();

        Assert.assertEquals(1, messages.size());

        final String message = messages.get(0);
        Assert.assertTrue(message.endsWith(" [com.softwareverde.logging.LoggerTests] Message."));
    }

    static class $Ignored$Symbol extends StaticInnerClass { }

    @Test
    public void should_log_messages_from_static_inner_with_symbol() {
        // Packages/Classes with "$" in their name are truncated before the symbol...

        // Setup
        final AnnotatedDebugLog debugLog = new AnnotatedDebugLog();
        Logger.LOG = debugLog;
        Logger.DEFAULT_LOG_LEVEL = LogLevel.ON;

        final $Ignored$Symbol staticInnerClass = new $Ignored$Symbol();

        // Action
        staticInnerClass.log("Message.");

        // Assert
        final List<String> messages = debugLog.getMessages();

        Assert.assertEquals(1, messages.size());

        final String message = messages.get(0);
        Assert.assertTrue(message.endsWith(" [com.softwareverde.logging.LoggerTests.StaticInnerClass] Message."));
    }
}
