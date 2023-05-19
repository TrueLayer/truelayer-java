package com.truelayer.java.http.interceptors.logging;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import java.util.List;
import lombok.Builder;
import okhttp3.internal.http2.Header;

@Builder
public class HttpLogMessage {
    protected static final String MESSAGE_FORMAT = "[HTTP|%s] %s %s %s %s headers=%s";

    private String idempotencyKey;

    private HttpLogPrefix prefix;

    private String method;

    private String url;

    private Integer code;

    private List<Header> headers;

    /**
     * Builder class for HTTP log messages
     */
    public static class HttpLogMessageBuilder {
        public String build() {
            return String.format(
                            MESSAGE_FORMAT,
                            idempotencyKey,
                            prefix,
                            isNotEmpty(code) ? code : "",
                            isNotEmpty(method) ? method : "",
                            url,
                            headers)
                    // removes extra white spaces due to values being null
                    .replaceAll("\\s+", " ");
        }
    }
}
