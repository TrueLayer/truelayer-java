package com.truelayer.java.http.interceptors.logging;

import com.truelayer.java.http.LoggerFactory;
import java.util.function.Consumer;
import org.tinylog.TaggedLogger;

/**
 * Default logging class for our library. Relies on <i>Tinylog</i> logging library.
 * @see <a href="https://tinylog.org/v2/"><i>Tinylog</i> docs</a>
 */
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
