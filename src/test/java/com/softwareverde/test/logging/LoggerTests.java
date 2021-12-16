package com.softwareverde.test.logging; // Needs to be outside of the com.softwareverde.logging package to trigger the proper stack stace...

import com.softwareverde.logging.Log;
import com.softwareverde.logging.LogFactory;
import com.softwareverde.logging.LogLevel;
import com.softwareverde.logging.Logger;
import com.softwareverde.logging.LoggerInstance;
import com.softwareverde.logging.log.AnnotatedLog;
import com.softwareverde.logging.log.SystemLog;
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

    static class StaticInnerClass {
        void log(final String message) {
            Logger.info(message);
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
    public static final LogFactory ORIGINAL_LOG_FACTORY = Logger.DEFAULT_LOG_FACTORY;

    protected static final String NEWLINE = System.lineSeparator();

    @Before
    public void setUp() {
        Logger.setLogLevel(ORIGINAL_LOG_LEVEL);
        Logger.setLogFactory(ORIGINAL_LOG_FACTORY);
        Logger.clearLogLevels();
    }

    @After
    public void tearDown() {
        Logger.setLogLevel(ORIGINAL_LOG_LEVEL);
        Logger.setLogFactory(ORIGINAL_LOG_FACTORY);
        Logger.clearLogLevels();
    }

    @Test
    public void should_log_messages_with_debug_default_level() {
        // Setup
        final DebugLog debugLog = new DebugLog();
        Logger.setLog(debugLog);
        Logger.setLogLevel(LogLevel.DEBUG);

        // Action
        Logger.debug("DEBUG");
        Logger.info("INFO");
        Logger.warn("WARN");
        Logger.error("ERROR");

        // Assert
        final List<DebugLog.Message> messages = debugLog.getMessages();

        Assert.assertEquals(4, messages.size());

        Assert.assertFalse(Logger.isTraceEnabled());

        Assert.assertTrue(Logger.isDebugEnabled());
        Assert.assertEquals("DEBUG", messages.get(0).message);
        Assert.assertEquals(LogLevel.DEBUG, messages.get(0).logLevel);

        Assert.assertTrue(Logger.isInfoEnabled());
        Assert.assertEquals("INFO", messages.get(1).message);
        Assert.assertEquals(LogLevel.INFO, messages.get(1).logLevel);

        Assert.assertTrue(Logger.isWarnEnabled());
        Assert.assertEquals("WARN", messages.get(2).message);
        Assert.assertEquals(LogLevel.WARN, messages.get(2).logLevel);

        Assert.assertTrue(Logger.isErrorEnabled());
        Assert.assertEquals("ERROR", messages.get(3).message);
        Assert.assertEquals(LogLevel.ERROR, messages.get(3).logLevel);
    }

    @Test
    public void should_not_log_messages_below_default_level() {
        // Setup
        final DebugLog debugLog = new DebugLog();
        Logger.setLog(debugLog);
        Logger.setLogLevel(LogLevel.WARN);

        // Action
        Logger.debug("DEBUG");
        Logger.info("INFO");
        Logger.warn("WARN");
        Logger.error("ERROR");

        // Assert
        final List<DebugLog.Message> messages = debugLog.getMessages();

        Assert.assertEquals(2, messages.size());

        Assert.assertFalse(Logger.isTraceEnabled());
        Assert.assertFalse(Logger.isDebugEnabled());
        Assert.assertFalse(Logger.isInfoEnabled());

        Assert.assertTrue(Logger.isWarnEnabled());
        Assert.assertEquals("WARN", messages.get(0).message);
        Assert.assertEquals(LogLevel.WARN, messages.get(0).logLevel);

        Assert.assertTrue(Logger.isErrorEnabled());
        Assert.assertEquals("ERROR", messages.get(1).message);
        Assert.assertEquals(LogLevel.ERROR, messages.get(1).logLevel);
    }

    @Test
    public void should_not_log_messages_with_OFF_default_level() {
        // Setup
        final DebugLog debugLog = new DebugLog();
        Logger.setLog(debugLog);
        Logger.setLogLevel(LogLevel.OFF);

        // Action
        Logger.debug("DEBUG");
        Logger.info("INFO");
        Logger.warn("WARN");
        Logger.error("ERROR");

        // Assert
        final List<DebugLog.Message> messages = debugLog.getMessages();

        Assert.assertEquals(0, messages.size());

        Assert.assertFalse(Logger.isTraceEnabled());
        Assert.assertFalse(Logger.isDebugEnabled());
        Assert.assertFalse(Logger.isInfoEnabled());
        Assert.assertFalse(Logger.isWarnEnabled());
        Assert.assertFalse(Logger.isErrorEnabled());
    }

    @Test
    public void should_log_messages_if_package_set_above_default_level() {
        // Setup
        final DebugLog debugLog = new DebugLog();
        Logger.setLog(debugLog);
        Logger.setLogLevel(LogLevel.WARN);

        Logger.setLogLevel(LoggerTests.class, LogLevel.DEBUG);

        // Action
        Logger.debug("DEBUG");
        Logger.info("INFO");
        Logger.warn("WARN");
        Logger.error("ERROR");

        // Assert
        final List<DebugLog.Message> messages = debugLog.getMessages();

        Assert.assertEquals(4, messages.size());

        Assert.assertFalse(Logger.isTraceEnabled());

        Assert.assertTrue(Logger.isDebugEnabled());
        Assert.assertEquals("DEBUG", messages.get(0).message);
        Assert.assertEquals(LogLevel.DEBUG, messages.get(0).logLevel);

        Assert.assertTrue(Logger.isInfoEnabled());
        Assert.assertEquals("INFO", messages.get(1).message);
        Assert.assertEquals(LogLevel.INFO, messages.get(1).logLevel);

        Assert.assertTrue(Logger.isWarnEnabled());
        Assert.assertEquals("WARN", messages.get(2).message);
        Assert.assertEquals(LogLevel.WARN, messages.get(2).logLevel);

        Assert.assertTrue(Logger.isErrorEnabled());
        Assert.assertEquals("ERROR", messages.get(3).message);
        Assert.assertEquals(LogLevel.ERROR, messages.get(3).logLevel);
    }

    @Test
    public void should_log_messages_if_separate_classes_have_different_log_levels() {
        // Setup
        final DebugLog thisDebugLog = new DebugLog();
        final DebugLog helperDebugLog = new DebugLog();
        Logger.setLogLevel(LogLevel.WARN);

        Logger.setLogLevel(LoggerTests.class, LogLevel.DEBUG);
        Logger.setLogLevel(LoggerTestsHelper.class, LogLevel.WARN);

        // Action
        Logger.setLog(thisDebugLog);
        {
            Logger.debug("THIS DEBUG");
            Logger.info("THIS INFO");
            Logger.warn("THIS WARN");
            Logger.error("THIS ERROR");
        }

        Logger.setLog(helperDebugLog);
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

            Assert.assertFalse(Logger.isTraceEnabled());

            Assert.assertTrue(Logger.isDebugEnabled());
            Assert.assertEquals("THIS DEBUG", thisMessages.get(0).message);
            Assert.assertEquals(LogLevel.DEBUG, thisMessages.get(0).logLevel);

            Assert.assertTrue(Logger.isInfoEnabled());
            Assert.assertEquals("THIS INFO", thisMessages.get(1).message);
            Assert.assertEquals(LogLevel.INFO, thisMessages.get(1).logLevel);

            Assert.assertTrue(Logger.isWarnEnabled());
            Assert.assertEquals("THIS WARN", thisMessages.get(2).message);
            Assert.assertEquals(LogLevel.WARN, thisMessages.get(2).logLevel);

            Assert.assertTrue(Logger.isErrorEnabled());
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
        Logger.setLog(debugLog);
        Logger.setLogLevel(LogLevel.OFF);

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

        Assert.assertFalse(Logger.isTraceEnabled());
        Assert.assertFalse(Logger.isDebugEnabled());
        Assert.assertFalse(Logger.isInfoEnabled());

        Assert.assertTrue(Logger.isWarnEnabled());
        Assert.assertEquals("WARN", messages.get(0).message);
        Assert.assertEquals(LogLevel.WARN, messages.get(0).logLevel);

        Assert.assertTrue(Logger.isErrorEnabled());
        Assert.assertEquals("ERROR", messages.get(1).message);
        Assert.assertEquals(LogLevel.ERROR, messages.get(1).logLevel);
    }

    @Test
    public void should_log_messages_from_static_inner_class() {
        // Setup
        final AnnotatedDebugLog debugLog = new AnnotatedDebugLog();
        Logger.setLog(debugLog);
        Logger.setLogLevel(LogLevel.ON);

        final StaticInnerClass staticInnerClass = new StaticInnerClass();

        // Action
        staticInnerClass.log("Message.");

        // Assert
        final List<String> messages = debugLog.getMessages();

        Assert.assertEquals(1, messages.size());

        final String message = messages.get(0);
        Assert.assertTrue(message.endsWith(" [com.softwareverde.test.logging.LoggerTests.StaticInnerClass] Message." + NEWLINE));
    }

    @Test
    public void should_log_messages_from_anonymous_class() {
        // Setup
        final AnnotatedDebugLog debugLog = new AnnotatedDebugLog();
        Logger.setLog(debugLog);
        Logger.setLogLevel(LogLevel.ON);

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
        Assert.assertTrue(message.endsWith(" [com.softwareverde.test.logging.LoggerTests] Message." + NEWLINE));
    }

    @Test
    public void should_log_messages_from_lambda() {
        // Setup
        final AnnotatedDebugLog debugLog = new AnnotatedDebugLog();
        Logger.setLog(debugLog);
        Logger.setLogLevel(LogLevel.ON);

        // Action
        final Runnable lambda = (() -> {
            Logger.info("Message.");
        });
        lambda.run();

        // Assert
        final List<String> messages = debugLog.getMessages();

        Assert.assertEquals(1, messages.size());

        final String message = messages.get(0);
        Assert.assertTrue(message.endsWith(" [com.softwareverde.test.logging.LoggerTests] Message." + NEWLINE));
    }

    static class $Ignored$Symbol extends StaticInnerClass { }

    @Test
    public void should_log_messages_from_static_inner_with_symbol() {
        // Packages/Classes with "$" in their name are truncated before the symbol...

        // Setup
        final AnnotatedDebugLog debugLog = new AnnotatedDebugLog();
        Logger.setLog(debugLog);
        Logger.setLogLevel(LogLevel.ON);

        final $Ignored$Symbol staticInnerClass = new $Ignored$Symbol();

        // Action
        staticInnerClass.log("Message.");

        // Assert
        final List<String> messages = debugLog.getMessages();

        Assert.assertEquals(1, messages.size());

        final String message = messages.get(0);
        Assert.assertTrue(message.endsWith(" [com.softwareverde.test.logging.LoggerTests.StaticInnerClass] Message." + NEWLINE));
    }

    @Test
    public void should_log_simple_messages_with_instance() {
        // Setup
        Logger.setLogLevel(Logger.DEFAULT_LOG_LEVEL);
        Logger.setLogFactory(Logger.DEFAULT_LOG_FACTORY);

        // Action
        LoggerInstance logger = Logger.getInstance(getClass());
        logger.trace("Test trace message");
        logger.debug("Test debug message");
        logger.info("Test info message.");
        logger.warn("Test warn message.", new Exception());
        logger.error("Test error message", new Exception());
    }

    @Test
    public void should_static_log_simple_messages_dynamic_log_level() {
        // Setup
        Logger.setLogLevel(LogLevel.ON);
        Logger.setLog(new AnnotatedLog(SystemLog.wrapSystemStream(System.out), SystemLog.wrapSystemStream(System.err)) {
            @Override
            protected String _getLogLevelAnnotation(final LogLevel logLevel) {
                return logLevel.name();
            }
        });

        // Action
        Logger.log(LogLevel.TRACE, "Test trace message");
        Logger.log(LogLevel.DEBUG, "Test debug message");
        Logger.log(LogLevel.INFO, "Test info message.");
        Logger.log(LogLevel.WARN, "Test warn message.", new Exception());
        Logger.log(LogLevel.ERROR, "Test error message", new Exception());
    }

    @Test
    public void should_log_simple_messages_dynamic_log_level() {
        // Setup
        Logger.setLogLevel(LogLevel.ON);
        Logger.setLogFactory(new LogFactory() {
            @Override
            public Log newLog(final Class<?> clazz) {
                return new AnnotatedLog(SystemLog.wrapSystemStream(System.out), SystemLog.wrapSystemStream(System.err)) {
                    @Override
                    protected String _getLogLevelAnnotation(final LogLevel logLevel) {
                        return logLevel.name();
                    }
                };
            }
        });

        // Action
        LoggerInstance logger = Logger.getInstance(getClass());
        logger.log(LogLevel.TRACE, "Test trace message");
        logger.log(LogLevel.DEBUG, "Test debug message");
        logger.log(LogLevel.INFO, "Test info message.");
        logger.log(LogLevel.WARN, "Test warn message.", new Exception());
        logger.log(LogLevel.ERROR, "Test error message", new Exception());
    }
}
