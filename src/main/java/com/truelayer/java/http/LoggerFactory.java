package com.truelayer.java.http;

import org.tinylog.Logger;
import org.tinylog.TaggedLogger;

public class LoggerFactory {
    private LoggerFactory() {}

    public static TaggedLogger NewHttpLogger() {
        return Logger.tag("HTTP");
    }
}
