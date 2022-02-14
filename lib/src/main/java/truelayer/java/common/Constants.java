package truelayer.java.common;

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
     * Configuration keys
     */
    public static final class ConfigurationKeys {
        public static final String AUTH_ENDPOINT_URL_LIVE = "tl.auth.endpoint.live";
        public static final String AUTH_ENDPOINT_URL_SANDBOX = "tl.auth.endpoint.sandbox";

        public static final String PAYMENTS_ENDPOINT_URL_LIVE = "tl.payments.endpoint.live";
        public static final String PAYMENTS_ENDPOINT_URL_SANDBOX = "tl.payments.endpoint.sandbox";
        public static final String PAYMENTS_SCOPES = "tl.payments.scopes";

        public static final String HPP_ENDPOINT_URL_LIVE = "tl.hpp.endpoint.live";
        public static final String HPP_ENDPOINT_URL_SANDBOX = "tl.hpp.endpoint.sandbox";
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
        public static final String USER_AGENT = "User-Agent";
        public static final String AUTHORIZATION = "Authorization";
        public static final String COOKIE = "Cookie";
        public static final String TL_CORRELATION_ID = "X-Tl-Correlation-Id";
    }
}
