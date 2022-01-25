package truelayer.java.http;

import org.tinylog.Logger;
import org.tinylog.TaggedLogger;

public class LoggerFactory {
    private LoggerFactory() {}

    public static TaggedLogger NewHttpLogger() {
        return Logger.tag("HTTP");
    }

    public static TaggedLogger NewSystemLogger() {
        return Logger.tag("SYSTEM");
    }
}
