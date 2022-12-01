package com.truelayer.java;

/**
 * Library constants class
 */
public final class Constants {

    /**
     * Oauth scopes
     */
    public static final class Scopes {
        public static final String PAYMENTS = "payments";
    }

    /**
     * Version info keys
     */
    public static final class VersionInfo {
        public static final String NAME = "name";
        public static final String VERSION = "version";
    }

    /**
     * HTTP headers names
     */
    public static final class HeaderNames {
        public static final String IDEMPOTENCY_KEY = "Idempotency-Key";
        public static final String TL_SIGNATURE = "Tl-Signature";
        public static final String TL_AGENT = "TL-Agent";
        public static final String AUTHORIZATION = "Authorization";
        public static final String COOKIE = "Cookie";
        public static final String TL_CORRELATION_ID = "X-Tl-Correlation-Id";
    }
}
