package com.softwareverde.logging.slf4j;

import com.softwareverde.logging.Log;
import com.softwareverde.logging.LogFactory;

public class Slf4jLogFactory implements LogFactory {
    @Override
    public Log newLog(final Class<?> aClass) {
        return new Slf4jLog(aClass);
    }
}
