package com.truelayer.java.http.interceptors.logging;

import com.truelayer.java.http.LoggerFactory;
import java.util.function.Consumer;
import org.tinylog.TaggedLogger;

public class DefaultLogConsumer implements Consumer<String> {
    private final TaggedLogger logger;

    public DefaultLogConsumer() {
        this.logger = LoggerFactory.NewHttpLogger();
    }

    @Override
    public void accept(String s) {
        logger.trace(s);
    }
}
